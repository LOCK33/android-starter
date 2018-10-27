# android-starter

## Available Methods Used in BaseActivity

| Name        | Description | Example or Advance |
| ------------- | ------------- | ----- |
| startActivity(Class<?>) | Go to a new activity | `startActivity(MainActivity.class)` |
| registerForContextMenu(ContextMenuInfo) | Register context menu for view like ListView |  |
| setActionMenu(@MenuRes int) | Set action menu for current activity | `setActionMenu(R.menu.main`) |
| setIcon(...) | Set icon for current activity | `setIcon(R.drawable.icon)` |
| exitApplication() | Exit current application | |
| info(String) | Toast a message | `info(R.string.message)` |
| alert(String, String, ApplicationUtils.Action) | Open an alert dialog |  |
| confirm(@StringRes int, @StringRes int, ApplicationUtils.Action, ApplicationUtils.Action) | Open a confirmation dialog | |
| registerProgressBar() | Register a progress bar in onCreate method | `showProgressBar()` or `hideProgressBar()` |
| bindObjectToView(Object[, String]) | Bind data to views (id starts with a prefix optionally) |  `bindObjectToView(user, "user_")` and `android:id="@+id/user_name"` |
| checkRequired(@IdRes int, @StringRes int) | Check the view whether value is empty | `checkRequired(R.id.edit_text, R.string.required)` |