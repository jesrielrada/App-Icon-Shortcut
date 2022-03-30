# App-Icon-Shortcut

#### Adding to your project:
1. Add this library as a submodule in your Android project
- `git submodule add https://github.com/jesrielrada/App-Icon-Shortcut.git`
- `git submodule update`
2. In Android Studio, add App-Icon-Shortcut as a new module
- `In settings.gradle add include 'App-Icon-Shortcut'`
- `And project(':App-Icon-Shortcut').projectDir = new File('Native-Preloading-Page')`
3. Add as a dependency in apps build.gradle file
- `implementation project(path: ':App-Icon-Shortcut')`


### How to use in your project:
1. Initialize variable in the top of your main activity and call "createAppIconShortcuts()" on onCreateMethod()
2. Init the shortcut model from the library to be able to call the "createAppIconShortcuts()"
- Note that the labels and resources are different for each operator also use the phone system language when inputting the labels as this will be displayed outside of te app
- Also note that shortcut icons is only available on API 25 and above 
3. Call "loadAppShortcutRoute()" when your webview is already loaded
- Note for CMS Native App: Call this method "loadAppShortcutRoute()" on pwaReady and on onNewIntent. Check first if intent was passed correctly before calling the method. 
- If route called is deposit nor withdrawal, just call the method "loadAppShortcutRoute()" on login callback and pass the pending route as the parameter for route. 
