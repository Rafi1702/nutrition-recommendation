package com.example.nutritiontracker.datasource.remote

import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.domain.MacroNutrients
import com.example.nutritiontracker.domain.MacroType

val MOCK_FOODS = listOf(
    Food(
        id = "1",
        name = "Nasi Goreng Spesial",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 45.0),
            MacroNutrients(MacroType.PROTEIN, 18.5),
            MacroNutrients(MacroType.FAT, 12.0),
            MacroNutrients(MacroType.CALORIES, 350.0)
        )
    ),
    Food(
        id = "2",
        name = "Mie Ayam Pangsit",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 52.0),
            MacroNutrients(MacroType.PROTEIN, 15.0),
            MacroNutrients(MacroType.FAT, 9.5),
            MacroNutrients(MacroType.CALORIES, 380.0)
        )
    ),
    Food(
        id = "3",
        name = "Sate Ayam Madura",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 10.0),
            MacroNutrients(MacroType.PROTEIN, 28.0),
            MacroNutrients(MacroType.FAT, 14.5),
            MacroNutrients(MacroType.CALORIES, 290.0)
        )
    ),
    Food(
        id = "4",
        name = "Gado-Gado Betawi",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 30.0),
            MacroNutrients(MacroType.PROTEIN, 12.0),
            MacroNutrients(MacroType.FAT, 15.0),
            MacroNutrients(MacroType.CALORIES, 280.0)
        )
    ),
    Food(
        id = "5",
        name = "Rendang Sapi",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 5.0),
            MacroNutrients(MacroType.PROTEIN, 24.0),
            MacroNutrients(MacroType.FAT, 22.5),
            MacroNutrients(MacroType.CALORIES, 310.0)
        )
    ),
    Food(
        id = "6",
        name = "Soto Ayam Kuning",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 15.0),
            MacroNutrients(MacroType.PROTEIN, 16.0),
            MacroNutrients(MacroType.FAT, 8.0),
            MacroNutrients(MacroType.CALORIES, 210.0)
        )
    ),
    Food(
        id = "7",
        name = "Bakso Malang",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 35.0),
            MacroNutrients(MacroType.PROTEIN, 19.0),
            MacroNutrients(MacroType.FAT, 11.0),
            MacroNutrients(MacroType.CALORIES, 310.0)
        )
    ),
    Food(
        id = "8",
        name = "Nasi Uduk Komplit",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 50.0),
            MacroNutrients(MacroType.PROTEIN, 14.0),
            MacroNutrients(MacroType.FAT, 18.0),
            MacroNutrients(MacroType.CALORIES, 420.0)
        )
    ),
    Food(
        id = "9",
        name = "Bubur Ayam",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 38.0),
            MacroNutrients(MacroType.PROTEIN, 12.5),
            MacroNutrients(MacroType.FAT, 6.0),
            MacroNutrients(MacroType.CALORIES, 260.0)
        )
    ),
    Food(
        id = "10",
        name = "Ketoprak Jakarta",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 48.0),
            MacroNutrients(MacroType.PROTEIN, 11.0),
            MacroNutrients(MacroType.FAT, 16.0),
            MacroNutrients(MacroType.CALORIES, 360.0)
        )
    ),
    Food(
        id = "11",
        name = "Pecel Lele",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 25.0),
            MacroNutrients(MacroType.PROTEIN, 22.0),
            MacroNutrients(MacroType.FAT, 17.0),
            MacroNutrients(MacroType.CALORIES, 340.0)
        )
    ),
    Food(
        id = "12",
        name = "Ikan Bakar Jimbaran",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 8.0),
            MacroNutrients(MacroType.PROTEIN, 32.0),
            MacroNutrients(MacroType.FAT, 10.0),
            MacroNutrients(MacroType.CALORIES, 260.0)
        )
    ),
    Food(
        id = "13",
        name = "Ayam Penyet",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 12.0),
            MacroNutrients(MacroType.PROTEIN, 30.0),
            MacroNutrients(MacroType.FAT, 16.0),
            MacroNutrients(MacroType.CALORIES, 330.0)
        )
    ),
    Food(
        id = "14",
        name = "Nasi Campur Bali",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 55.0),
            MacroNutrients(MacroType.PROTEIN, 25.0),
            MacroNutrients(MacroType.FAT, 19.0),
            MacroNutrients(MacroType.CALORIES, 480.0)
        )
    ),
    Food(
        id = "15",
        name = "Rawon Daging",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 14.0),
            MacroNutrients(MacroType.PROTEIN, 26.0),
            MacroNutrients(MacroType.FAT, 15.0),
            MacroNutrients(MacroType.CALORIES, 300.0)
        )
    ),
    Food(
        id = "16",
        name = "Siomay Bandung",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 32.0),
            MacroNutrients(MacroType.PROTEIN, 16.0),
            MacroNutrients(MacroType.FAT, 11.0),
            MacroNutrients(MacroType.CALORIES, 280.0)
        )
    ),
    Food(
        id = "17",
        name = "Batagor",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 36.0),
            MacroNutrients(MacroType.PROTEIN, 14.0),
            MacroNutrients(MacroType.FAT, 18.0),
            MacroNutrients(MacroType.CALORIES, 350.0)
        )
    ),
    Food(
        id = "18",
        name = "Martabak Telur",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 40.0),
            MacroNutrients(MacroType.PROTEIN, 15.0),
            MacroNutrients(MacroType.FAT, 25.0),
            MacroNutrients(MacroType.CALORIES, 430.0)
        )
    ),
    Food(
        id = "19",
        name = "Gulai Kambing",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 10.0),
            MacroNutrients(MacroType.PROTEIN, 24.0),
            MacroNutrients(MacroType.FAT, 28.0),
            MacroNutrients(MacroType.CALORIES, 390.0)
        )
    ),
    Food(
        id = "20",
        name = "Es Campur",
        macros = listOf(
            MacroNutrients(MacroType.CARBS, 45.0),
            MacroNutrients(MacroType.PROTEIN, 3.0),
            MacroNutrients(MacroType.FAT, 4.0),
            MacroNutrients(MacroType.CALORIES, 220.0)
        )
    )
)