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

![Splash](https://raw.githubusercontent.com/bndynet/android-starter/master/docs/images/splash.png)

![Form](https://raw.githubusercontent.com/bndynet/android-starter/master/docs/images/form.png)