# Redirect Home Page with User Information

In this tutorial, we are going to learn how to create a functionality to redirect the user to the homepage with all information whenever registered.

## Activity (HomeActivity)

Now, we have to create an activity for **home** page. So lets right click to the **com.example.user.projectname** in the **app/java** folder to create our activity and layouts automatically.

After you've clicked to specified folder then follow the steps respectively.

1. Select New
2. Select New/Activity
3. Select New/Activity/Navigation Drawer Activity

Then a screen will be appeared and in this screen write an **Activity Name** as **HomeActivity** and press to the **OK** to create the files.

## After Registered Redirect To HomeActivity

As you know, In the previous tutorial, we have learned how to register user. So now lets add following lines to the **register** functions.

~~~~
	
	...

    if(TextUtils.isEmpty(user.getError_msg())) {
        Toast.makeText(MainActivity.this,"User register succesfully",Toast.LENGTH_SHORT).show();

        //Assign json result to currentUser -> will implement
        Common.currentUser = response.body();

        //Start new activity
        startActivity(new Intent(MainActivity.this,HomeActivity.class));

        //Close this activity which means current page
        finish();
    }

    ...

~~~~

## Custom Object - (Common/User)

In the **Common** class we have to create a custom object which will be instantiated from **User** model to hold current user informations.

So lets add the following lines to this file.

~~~~

    public static User currentUser = null;

~~~~

Because of that **User** object (variable) is **static** we will be able to access to it from anywhere in the application.

## HomeActivity - Get User Information

In the **HomeActivity** class to ge the **currentUser** informations add the following lines.

~~~~

    TextView txt_name, txt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ....

        //To get the headerView in the navigation Drawer
        View headerView = navigationView.getHeaderView(0);
        txt_name = (TextView) headerView.findViewById(R.id.txt_name);
        txt_phone = (TextView) headerView.findViewById(R.id.txt_phone);

        //Set Info - get info from Common class because User object is static there
        txt_name.setText(Common.currentUser.getName());
        txt_phone.setText(Common.currentUser.getEmail());
    }

~~~~

## Layout Design - (activity_home.xml)

Lets make a little change on the **activity_home.xml** file to change the background colour of the **navigational drawer**.

So, lets add the following lines to this file.

~~~~

<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/bg" // new line
    android:fitsSystemWindows="true"
    tools:openDrawer="start">

    <include
        layout="@layout/app_bar_home"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <android.support.design.widget.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:fitsSystemWindows="true"
        android:background="@color/navBackground" //new line
        app:headerLayout="@layout/nav_header_home"
        app:menu="@menu/activity_home_drawer"
        app:itemIconTint="@android:color/white"
        app:itemTextColor="@android:color/white"/>

</android.support.v4.widget.DrawerLayout>


~~~~

## Layout Design - (nav_header_home.xml)

In the **nav_header_home.xml** file we have to assign **id** to some elements to change their text in the **HomeActivity**.

So lets add the following lines to this file.

~~~~

    <TextView
        android:id="@+id/txt_name" //new line
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingTop="@dimen/nav_header_vertical_spacing"
        android:text="@string/nav_header_title"
        android:textAppearance="@style/TextAppearance.AppCompat.Body1" />

    <TextView
        android:id="@+id/text_email" //new line
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/nav_header_subtitle" />

~~~~

## Colors (colors.xml)

In the **colors.xml** lets create another color for backgroun of the **nagivation view**.

So, lets add the following lines to this file.

~~~~

    <color name="navBackground">#55d4e7e1</color>

~~~~




