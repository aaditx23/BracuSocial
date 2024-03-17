package com.tutorial.bracusocial

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tutorial.bracusocial.data.entities.User
import com.tutorial.bracusocial.data.UserDatabase
import com.tutorial.bracusocial.fragments.PrePreRegFragment
import com.tutorial.bracusocial.fragments.RoutineFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var bnv: BottomNavigationView
    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if (!isGranted){
                    Toast.makeText(this@MainActivity, "Permission Denied $permissionName", Toast.LENGTH_SHORT).show()
                    showRationaleDialog("Permission Denied", "Permissions are required to use the application. Please grant them manually.")
                    return@registerForActivityResult
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        createUserOne()


        bnv = findViewById(R.id.bottomNavigationView)
        bnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId){
                R.id.preprereg ->{
                    replaceFragment(PrePreRegFragment())
                    true
                }
                R.id.routine ->{
                    replaceFragment(RoutineFragment())
                    true
                }
                else -> false

            }
        }
        replaceFragment(PrePreRegFragment())

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }






    private fun requestRequiredPermission(){
        requestPermission.launch(
            arrayOf(
//                Manifest.permission.INTERNET,

            )
        )
    }

    private fun showRationaleDialog(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
        builder.create().show()
    }

    private fun createUserOne(){
        CoroutineScope(Dispatchers.IO).launch {
            async{
                val dao = UserDatabase.getInstance(this@MainActivity).dao
                val user = dao.getCurrentUser(1)
                if (user == null){
                    val newUserData = User(
                        id = 1,
                        studentID = 23341077,
                        name = "Aadit",
                        courses = mutableMapOf(),
                        friends = mutableListOf(),
                        password = "123"
                    )
                    dao.upsertUser(newUserData)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity, "User 1 created", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity, "User Exists", Toast.LENGTH_SHORT).show()
                    }
                }
            }.await()
        }
    }

    private fun checkDatabase(): Boolean{
        var flag = false
        CoroutineScope(Dispatchers.IO).launch {
            val dao = UserDatabase.getInstance(this@MainActivity).dao
            val user = dao.getCourseList()
            if (user.isEmpty()){
                flag = false
            }
            else{
                flag = true
            }
        }

        return flag
    }

}