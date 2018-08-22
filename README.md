# CustomNavigationDrawerKotlin

in project level build.gradle add following

```
allprojects {
    repositories {
       --------        
       --------
        maven { url 'https://jitpack.io' } 
    }
  }
```
under depedencies add the follwing gradle pllugin

```
dependencies {
    -----
    implementation 'com.github.venkateshguddanti:LoginComponent:0.0.1'
}
```
The basic usage is given in above repository.
