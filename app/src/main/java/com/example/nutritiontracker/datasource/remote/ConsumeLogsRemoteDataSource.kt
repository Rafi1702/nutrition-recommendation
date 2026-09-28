package com.example.nutritiontracker.datasource.remote

import com.example.nutritiontracker.domain.ConsumeLog
import com.example.nutritiontracker.domain.Food
import com.example.nutritiontracker.domain.MacroNutrients
import com.example.nutritiontracker.domain.MacroType


val MOCK_CONSUME_LOGS = listOf(
    ConsumeLog(
        date = "2026-05-01",
        breakfast = listOf(
            Food(
                id = "food_1",
                name = "Oatmeal dengan Susu",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 10.0),
                    MacroNutrients(MacroType.CARBS, 45.0),
                    MacroNutrients(MacroType.FAT, 5.0)
                )
            )
        ),
        lunch = listOf(
            Food(
                id = "food_2",
                name = "Nasi Ayam Dada Bakar",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 35.0),
                    MacroNutrients(MacroType.CARBS, 50.0),
                    MacroNutrients(MacroType.FAT, 8.0)
                )
            )
        ),
        dinner = listOf(
            Food(
                id = "food_3",
                name = "Sup Telur Tomat",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 12.0),
                    MacroNutrients(MacroType.CARBS, 10.0),
                    MacroNutrients(MacroType.FAT, 7.0)
                )
            )
        )
    ),
    ConsumeLog(
        date = "2026-05-02",
        breakfast = listOf(
            Food(
                id = "food_4",
                name = "Roti Gandum & Peanut Butter",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 8.0),
                    MacroNutrients(MacroType.CARBS, 30.0),
                    MacroNutrients(MacroType.FAT, 14.0)
                )
            )
        ),
        lunch = listOf(
            Food(
                id = "food_5",
                name = "Beef Steak & Kentang Tumbuk",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 40.0),
                    MacroNutrients(MacroType.CARBS, 40.0),
                    MacroNutrients(MacroType.FAT, 18.0)
                )
            )
        ),
        dinner = emptyList()
    ),
    ConsumeLog(
        date = "2026-05-03",
        breakfast = listOf(
            Food(
                id = "food_6",
                name = "Pancake Pisang",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 6.0),
                    MacroNutrients(MacroType.CARBS, 35.0),
                    MacroNutrients(MacroType.FAT, 6.0)
                )
            )
        ),
        lunch = emptyList(),
        dinner = listOf(
            Food(
                id = "food_7",
                name = "Salad Tuna",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 25.0),
                    MacroNutrients(MacroType.CARBS, 12.0),
                    MacroNutrients(MacroType.FAT, 10.0)
                )
            )
        )
    ),
    ConsumeLog(
        date = "2026-05-04",
        breakfast = emptyList(),
        lunch = listOf(
            Food(
                id = "food_8",
                name = "Nasi Goreng Spesial",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 18.0),
                    MacroNutrients(MacroType.CARBS, 65.0),
                    MacroNutrients(MacroType.FAT, 15.0)
                )
            )
        ),
        dinner = emptyList()
    ),
    ConsumeLog(
        date = "2026-05-05",
        breakfast = listOf(
            Food(
                id = "food_9",
                name = "Greek Yogurt & Granola",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 15.0),
                    MacroNutrients(MacroType.CARBS, 28.0),
                    MacroNutrients(MacroType.FAT, 4.0)
                )
            )
        ),
        lunch = emptyList(),
        dinner = listOf(
            Food(
                id = "food_10",
                name = "Salmon Panggang & Asparagus",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 34.0),
                    MacroNutrients(MacroType.CARBS, 8.0),
                    MacroNutrients(MacroType.FAT, 16.0)
                )
            )
        )
    ),
    ConsumeLog(
        date = "2026-05-06",
        breakfast = emptyList(),
        lunch = listOf(
            Food(
                id = "food_11",
                name = "Mie Goreng Ayam",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 16.0),
                    MacroNutrients(MacroType.CARBS, 70.0),
                    MacroNutrients(MacroType.FAT, 14.0)
                )
            )
        ),
        dinner = emptyList()
    ),
    ConsumeLog(
        date = "2026-05-07",
        breakfast = listOf(
            Food(
                id = "food_12",
                name = "Telur Orak-arik & Roti Panggang",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 14.0),
                    MacroNutrients(MacroType.CARBS, 22.0),
                    MacroNutrients(MacroType.FAT, 11.0)
                )
            )
        ),
        lunch = emptyList(),
        dinner = listOf(
            Food(
                id = "food_13",
                name = "Sup Ayam Sayur",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 22.0),
                    MacroNutrients(MacroType.CARBS, 15.0),
                    MacroNutrients(MacroType.FAT, 5.0)
                )
            )
        )
    ),
    ConsumeLog(
        date = "2026-05-08",
        breakfast = emptyList(),
        lunch = listOf(
            Food(
                id = "food_14",
                name = "Sate Ayam Lontong",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 30.0),
                    MacroNutrients(MacroType.CARBS, 55.0),
                    MacroNutrients(MacroType.FAT, 16.0)
                )
            )
        ),
        dinner = emptyList()
    ),
    ConsumeLog(
        date = "2026-05-09",
        breakfast = listOf(
            Food(
                id = "food_15",
                name = "Smoothie Bowl Buah",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 8.0),
                    MacroNutrients(MacroType.CARBS, 42.0),
                    MacroNutrients(MacroType.FAT, 3.0)
                )
            )
        ),
        lunch = emptyList(),
        dinner = emptyList()
    ),
    ConsumeLog(
        date = "2026-05-10",
        breakfast = emptyList(),
        lunch = emptyList(),
        dinner = listOf(
            Food(
                id = "food_16",
                name = "Daging Sapi Lada Hitam",
                macros = listOf(
                    MacroNutrients(MacroType.PROTEIN, 32.0),
                    MacroNutrients(MacroType.CARBS, 25.0),
                    MacroNutrients(MacroType.FAT, 14.0)
                )
            )
        )
    )
)