# kotlin-javafx-base


## How to



### Step 1. Add the JitPack repository to your build file

Add it in build.gradle

```kotlin
repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()

    maven("https://jitpack.io")
}
```


### Step 2. Add the dependency

```kotlin

dependencies {
    // ...

    // add the dependency
    implementation("com.github.xingray:kotlin-javafx-base:0.0.4")
}
```