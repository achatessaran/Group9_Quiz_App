package com.example.quizapp

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val difficulty: String
)

val quizQuestions = listOf(

    // Easy Questions
    Question("What does CPU stand for?", listOf("Central Processing Unit", "Computer Personal Unit", "Central Program Utility", "Control Processing User"), "Central Processing Unit", "Easy"),
    Question("Which language is officially supported for Android development?", listOf("Kotlin", "Swift", "Ruby", "PHP"), "Kotlin", "Easy"),
    Question("What does RAM stand for?", listOf("Random Access Memory", "Read Access Memory", "Rapid Action Machine", "Run Access Module"), "Random Access Memory", "Easy"),
    Question("Which company develops Android?", listOf("Google", "Apple", "Microsoft", "Samsung"), "Google", "Easy"),
    Question("What is an app?", listOf("Application software", "Hardware device", "Network cable", "Battery type"), "Application software", "Easy"),
    Question("Which file type is commonly used for Android apps?", listOf("APK", "DOCX", "MP3", "JPEG"), "APK", "Easy"),
    Question("What does UI stand for?", listOf("User Interface", "Universal Input", "User Internet", "Unit Integration"), "User Interface", "Easy"),
    Question("Which device is mainly used to test Android apps virtually?", listOf("Emulator", "Printer", "Router", "Scanner"), "Emulator", "Easy"),
    Question("What is a button used for in an app?", listOf("To perform an action", "To store data only", "To charge the phone", "To increase RAM"), "To perform an action", "Easy"),
    Question("Which screen usually appears first in an app?", listOf("Home Screen", "Result Screen", "Error Screen", "Settings Screen"), "Home Screen", "Easy"),
    Question("What does IDE stand for?", listOf("Integrated Development Environment", "Internet Design Engine", "Internal Data Entry", "Input Device Emulator"), "Integrated Development Environment", "Easy"),
    Question("Which tool are we using to build this app?", listOf("Android Studio", "Microsoft Word", "Photoshop", "Excel"), "Android Studio", "Easy"),
    Question("What is Kotlin?", listOf("Programming language", "Database", "Web browser", "Operating system"), "Programming language", "Easy"),
    Question("What is a quiz app mainly used for?", listOf("Testing knowledge", "Editing photos", "Sending parcels", "Playing music only"), "Testing knowledge", "Easy"),
    Question("What does mobile programming focus on?", listOf("Building mobile apps", "Repairing hardware", "Designing buildings", "Writing novels"), "Building mobile apps", "Easy"),

    // Medium Questions
    Question("What is Jetpack Compose?", listOf("Modern Android UI toolkit", "Database system", "Cloud storage", "Operating system"), "Modern Android UI toolkit", "Medium"),
    Question("Which annotation is used for Compose UI functions?", listOf("@Composable", "@Override", "@Entity", "@Android"), "@Composable", "Medium"),
    Question("What is Navigation Compose used for?", listOf("Moving between screens", "Saving images", "Increasing battery", "Compiling Java only"), "Moving between screens", "Medium"),
    Question("What is state in Compose?", listOf("Data that can change UI", "Phone location", "App icon", "XML file only"), "Data that can change UI", "Medium"),
    Question("Which function remembers state during recomposition?", listOf("remember", "forget", "saveFile", "launchApp"), "remember", "Medium"),
    Question("What does recomposition mean in Compose?", listOf("UI redraw after state change", "Deleting app data", "Restarting phone", "Installing emulator"), "UI redraw after state change", "Medium"),
    Question("Which layout arranges items vertically?", listOf("Column", "Row", "Box", "Card"), "Column", "Medium"),
    Question("Which layout arranges items horizontally?", listOf("Row", "Column", "Text", "Button"), "Row", "Medium"),
    Question("Which Compose component displays text?", listOf("Text", "Button", "Image", "Card"), "Text", "Medium"),
    Question("Which Compose component is used for clickable actions?", listOf("Button", "Column", "Theme", "Package"), "Button", "Medium"),
    Question("What is Material Design?", listOf("Design system for UI", "Database model", "Testing library", "Programming language"), "Design system for UI", "Medium"),
    Question("What is Gradle used for in Android projects?", listOf("Build and dependency management", "Drawing images", "Typing notes", "Phone charging"), "Build and dependency management", "Medium"),
    Question("What is a package name?", listOf("Unique app code namespace", "Phone model", "Button color", "Image size"), "Unique app code namespace", "Medium"),
    Question("What is an emulator?", listOf("Virtual Android device", "Cloud database", "Keyboard layout", "App store account"), "Virtual Android device", "Medium"),
    Question("What is the purpose of a data class in Kotlin?", listOf("To hold data", "To draw UI only", "To install apps", "To create passwords only"), "To hold data", "Medium"),

    // Hard Questions
    Question("What happens when Compose state changes?", listOf("Affected composables may recompose", "The app is uninstalled", "The phone restarts", "Gradle is deleted"), "Affected composables may recompose", "Hard"),
    Question("Why should UI state be managed carefully?", listOf("To keep UI consistent", "To reduce phone brightness", "To increase file size", "To disable emulator"), "To keep UI consistent", "Hard"),
    Question("What is a NavHost in Navigation Compose?", listOf("Container for navigation destinations", "Database table", "Image processor", "Keyboard controller"), "Container for navigation destinations", "Hard"),
    Question("What is a composable destination?", listOf("A screen route in navigation", "A hard disk folder", "An APK signature", "A device driver"), "A screen route in navigation", "Hard"),
    Question("Why is hardcoding questions acceptable in this assignment?", listOf("Because local quiz data is enough for the scope", "Because databases are not allowed in Android", "Because Kotlin cannot use APIs", "Because Compose cannot store state"), "Because local quiz data is enough for the scope", "Hard"),
    Question("What problem can occur if multiple users edit MainActivity at the same time?", listOf("Merge conflicts", "Better performance", "Automatic testing", "More storage"), "Merge conflicts", "Hard"),
    Question("Which Kotlin feature helps avoid null pointer errors?", listOf("Null safety", "XML layout", "Gradle sync", "APK signing"), "Null safety", "Hard"),
    Question("What is the role of Modifier in Compose?", listOf("To decorate or configure UI elements", "To delete functions", "To start emulator", "To compile only Java files"), "To decorate or configure UI elements", "Hard"),
    Question("Why should a quiz app track user answers?", listOf("To support result review", "To increase screen brightness", "To reduce APK size", "To change phone language"), "To support result review", "Hard"),
    Question("What is the benefit of separating screens into different files?", listOf("Cleaner and maintainable code", "Slower build time", "More bugs", "Harder navigation"), "Cleaner and maintainable code", "Hard"),
    Question("What does unidirectional data flow mean in UI apps?", listOf("State flows down and events flow up", "Data moves randomly", "Only database can change UI", "Screens cannot communicate"), "State flows down and events flow up", "Hard"),
    Question("What is the risk of adding Firebase near the deadline?", listOf("Increased complexity and integration risk", "No internet required", "Less code needed", "Automatic marks"), "Increased complexity and integration risk", "Hard"),
    Question("Why should the app be tested on an emulator?", listOf("To verify functionality before submission", "To create quiz questions", "To write README only", "To change package name"), "To verify functionality before submission", "Hard"),
    Question("What is the purpose of selected answer highlighting?", listOf("To improve user feedback", "To reduce app size", "To disable navigation", "To hide questions"), "To improve user feedback", "Hard"),
    Question("Why is a progress bar useful in a quiz app?", listOf("It shows quiz completion progress", "It stores passwords", "It connects to Firebase", "It improves CPU speed"), "It shows quiz completion progress", "Hard")
)