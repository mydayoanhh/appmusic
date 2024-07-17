plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.musicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.hbb20:ccp:2.5.2")
    implementation ("com.google.firebase:firebase-analytics:21.0.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation("com.google.firebase:firebase-database:20.0.4")
    implementation("com.firebaseui:firebase-ui-database:8.0.1")


}
apply(plugin = "com.google.gms.google-services")
apply(plugin = "com.android.application")
