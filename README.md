# android-starter

## Available Methods Used in BaseActivity

| Name        | Description | Example or Advance |
| ------------- | ------------- | ----- |
| startActivity(Class<?> cls) | Go to a new activity | `startActivity(MainActivity.class)` |
| registerForContextMenu(ContextMenuInfo contextMenuInfo) | Register context menu for view like ListView |  |
| setActionMenu(@MenuRes int menu) | Set action menu for current activity | `setActionMenu(R.menu.main`) |
| setIcon(...) | Set icon for current activity | `setIcon(R.drawable.icon` |
| exitApplication() | Exit current application | |
| info(String message) | Toast a message | `info(R.string.message)` |
| alert(String title, String message, ApplicationUtils.Action action) | Open an alert dialog |  |
| confirm(@StringRes int title, @StringRes int message, ApplicationUtils.Action actionYes, ApplicationUtils.Action actionNo) | Open a confirmation dialog | |
| registerProgressBar() | Register a progress bar in onCreate method | `showProgressBar()` or `hideProgressBar()` |
| bindObjectToView(Object data[, String viewIdPrefix]) | Bind data to views (id starts with viewIdPrefix optionally) |  `bindObjectToView(user, "user_")` and `android:id="@+id/user_name"` |
| checkRequired(@IdRes int viewId, @StringRes int requiredMessage) | Check the view whether value is empty | `checkRequired(R.id.edit_text, R.string.required)` |