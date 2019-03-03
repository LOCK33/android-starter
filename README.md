# android-starter

## Available Methods Used in BaseActivity

| Name        | Description | Misc |
| ------------- | ------------- | ----- |
| startActivity(Class<? extends Activity>) | Go to a new activity | `startActivity(MainActivity.class)` |
| registerForContextMenu(ContextMenuInfo) | Register context menu for view like ListView |  |
| setActionMenu(@MenuRes) | Set action menu for current activity | `setActionMenu(R.menu.main`) |
| setIcon(...) | Set icon for current activity | `setIcon(R.drawable.icon)` |
| exitApplication() | Exit current application | |
| info(String) | Toast a message | `info(R.string.message)` |
| alert(String, String, ApplicationUtils.Action) | Open an alert dialog |  |
| confirm(@StringRes, @StringRes, ApplicationUtils.Action, ApplicationUtils.Action) | Open a confirmation dialog | |
| registerProgressBar() | Register a progress bar in onCreate method | `showProgressBar()` or `hideProgressBar()` |
| bindObjectToView(Object[, String]) | Bind data to views (id starts with a prefix optionally) |  `bindObjectToView(user, "user_")` and `android:id="@+id/user_name"` |
| boolean checkRequired(@IdRes, @StringRes) | Check the view whether value is empty | `checkRequired(R.id.edit_text, R.string.required)` |

## Screenshots

![Login](https://raw.githubusercontent.com/bndynet/android-starter/master/docs/images/login.png)

![Form](https://raw.githubusercontent.com/bndynet/android-starter/master/docs/images/form.png)

![List](https://raw.githubusercontent.com/bndynet/android-starter/master/docs/images/list.png)

## Debug database with emulator

Run the command in the terminal - `adb forward tcp:8080 tcp:8080` and open http://localhost:8080

Note : If you want use different port other than 8080. In the app build.gradle file under buildTypes do the following change

```
debug {
    resValue("string", "PORT_NUMBER", "8081")
}

```

More information please visit https://github.com/amitshekhariitbhu/Android-Debug-Database#using-android-debug-database-library-in-your-application
