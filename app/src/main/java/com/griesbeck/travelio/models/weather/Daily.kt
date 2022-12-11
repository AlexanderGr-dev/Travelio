package com.griesbeck.travelio.models.weather

data class Daily (
        val time: List<String>,
        val weathercode: List<Int>,
        val temperature_2m_max: List<Double>,
        val temperature_2m_min: List<Double>
        )
