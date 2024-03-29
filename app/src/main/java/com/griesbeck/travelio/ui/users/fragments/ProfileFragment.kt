package com.griesbeck.travelio.ui.users.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.griesbeck.travelio.databinding.FragmentProfileBinding
import com.griesbeck.travelio.models.users.User
import com.griesbeck.travelio.helpers.showImagePicker
import com.griesbeck.travelio.ui.users.activities.SignInActivity
import com.griesbeck.travelio.ui.users.viewmodels.UsersViewModel
import com.squareup.picasso.Picasso

class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private val binding get() = _binding!!
    private var user: User = User()
    private val usersViewModel: UsersViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        usersViewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
            bindProfile()
        }


        binding.btnChangeProfilePicture.setOnClickListener {
            showImagePicker(imageIntentLauncher,it.context)
        }

        registerImagePickerCallback()

        binding.btnUpdateProfile.setOnClickListener {
            user.email = binding.etProfileEmail.text.toString()
            user.name = binding.etProfileName.text.toString()
            usersViewModel.updateUser(user)
        }

        binding.btnDeleteAccount.setOnClickListener {
            deleteProfileDialog()
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindProfile(){
        if(user.name.isNotEmpty()){
            binding.etProfileName.setText(user.name)
        }
        binding.etProfileEmail.setText(user.email)
        if(user.photo != ""){
            Picasso.get().load(user.photo.toUri()).into(binding.ivProfilePicture)
        }
    }

    private fun deleteProfileDialog() {
        val builder = MaterialAlertDialogBuilder(requireView().context)
        builder.setTitle("Delete Account")
        builder.setMessage("Do you really want to delete your Account? All trips will be lost.")
        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setPositiveButton("Delete") { dialog, _ ->
            usersViewModel.deleteCurrentUser(requireView().context,user)
        val signInIntent = Intent(view?.context, SignInActivity::class.java)
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        dialog.dismiss()
        startActivity(signInIntent)
        }
        builder.show()
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {

                            val photo = result.data!!.data!!

                            requireActivity().contentResolver.takePersistableUriPermission(
                                photo,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            user.photo = photo.toString()

                            Picasso.get()
                                .load(user.photo.toUri())
                                .into(binding.ivProfilePicture)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        Toast.makeText(view?.context, "Task cancelled", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(view?.context, "Some error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
