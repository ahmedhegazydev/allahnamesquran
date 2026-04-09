package app.asmaquran.mobile.features.settings

enum class SettingsLanguageOption(
    val storageValue: String,
    val localeTag: String
) {
    ARABIC(
        storageValue = "ar",
        localeTag = "ar"
    ),
    ENGLISH(
        storageValue = "en",
        localeTag = "en"
    );

    companion object {
        fun fromStorage(value: String?): SettingsLanguageOption {
            return entries.firstOrNull { it.storageValue == value } ?: ARABIC
        }
    }
}

enum class SettingsAppearanceOption(
    val storageValue: String
) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromStorage(value: String?): SettingsAppearanceOption {
            return entries.firstOrNull { it.storageValue == value } ?: SYSTEM
        }
    }
}
