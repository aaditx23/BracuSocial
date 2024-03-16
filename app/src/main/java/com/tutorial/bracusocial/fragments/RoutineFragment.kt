package com.tutorial.bracusocial.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tutorial.bracusocial.data.UserDatabase
import com.tutorial.bracusocial.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutineFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var table: Array<Array<TextView>>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRoutine()

        table = arrayOf(
            arrayOf(view.findViewById(R.id.slot1_1), view.findViewById(R.id.slot1_2), view.findViewById(R.id.slot1_3), view.findViewById(R.id.slot1_4), view.findViewById(R.id.slot1_5), view.findViewById(R.id.slot1_6), view.findViewById(R.id.slot1_7)),
            arrayOf(view.findViewById(R.id.slot2_1), view.findViewById(R.id.slot2_2), view.findViewById(R.id.slot2_3), view.findViewById(R.id.slot2_4), view.findViewById(R.id.slot2_5), view.findViewById(R.id.slot2_6), view.findViewById(R.id.slot2_7)),
            arrayOf(view.findViewById(R.id.slot3_1), view.findViewById(R.id.slot3_2), view.findViewById(R.id.slot3_3), view.findViewById(R.id.slot3_4), view.findViewById(R.id.slot3_5), view.findViewById(R.id.slot3_6), view.findViewById(R.id.slot3_7)),
            arrayOf(view.findViewById(R.id.slot4_1), view.findViewById(R.id.slot4_2), view.findViewById(R.id.slot4_3), view.findViewById(R.id.slot4_4), view.findViewById(R.id.slot4_5), view.findViewById(R.id.slot4_6), view.findViewById(R.id.slot4_7)),
            arrayOf(view.findViewById(R.id.slot5_1), view.findViewById(R.id.slot5_2), view.findViewById(R.id.slot5_3), view.findViewById(R.id.slot5_4), view.findViewById(R.id.slot5_5), view.findViewById(R.id.slot5_6), view.findViewById(R.id.slot5_7)),
            arrayOf(view.findViewById(R.id.slot6_1), view.findViewById(R.id.slot6_2), view.findViewById(R.id.slot6_3), view.findViewById(R.id.slot6_4), view.findViewById(R.id.slot6_5), view.findViewById(R.id.slot6_6), view.findViewById(R.id.slot6_7)),
            arrayOf(view.findViewById(R.id.slot7_1), view.findViewById(R.id.slot7_2), view.findViewById(R.id.slot7_3), view.findViewById(R.id.slot7_4), view.findViewById(R.id.slot7_5), view.findViewById(R.id.slot7_6), view.findViewById(R.id.slot7_7)),
            arrayOf(view.findViewById(R.id.slot8_1), view.findViewById(R.id.slot8_2), view.findViewById(R.id.slot8_3), view.findViewById(R.id.slot8_4), view.findViewById(R.id.slot8_5), view.findViewById(R.id.slot8_6), view.findViewById(R.id.slot8_7))
        )


    }

    private fun setTextToField(tv: TextView){
        val dao = UserDatabase.getInstance(requireContext()).dao
        CoroutineScope(Dispatchers.IO).launch {
            val user = dao.getCurrentUser(1)
            withContext(Dispatchers.Main){
                if (user != null) {
                    tv.text = user.courses.toString()
                }
            }
        }
    }

    private fun setRoutine(){
        val dao = UserDatabase.getInstance(requireContext()).dao
        CoroutineScope(Dispatchers.Main).launch {
            val user = dao.getCurrentUser(1)
            //withContext(Dispatchers.Main){
                if (user != null) {
                    val courseMap = user.courses
                    for ((courseNameWithSection, slotMap) in courseMap){
                        val temp = courseNameWithSection.split(" ")
                        val courseName = temp[0].trim()
                        val section = temp[1].trim()
                        if (slotMap.size >2){
                            println("$courseNameWithSection ${slotMap.keys}")
                            val labRow = slotMap["labRow"]
                            val labColumn = slotMap["labColumn"]
                            for(c in labColumn!!){
                                for (r in labRow!!){
                                    println("Lab slots in labRow: $r labColumn $c")
                                    val currentText = table[r][c].text.toString()
                                    if (currentText != "-"){
                                        table[r][c].text = String.format("%s\n%s-LAB %s", currentText, courseName, section)
                                        table[r][c].setBackgroundResource(R.drawable.table_box_red)
                                    }
                                    else{
                                        table[r][c].text = String.format("%s-LAB %s", courseName, section)
                                        table[r][c].setBackgroundResource(R.drawable.table_box_green)
                                    }
                                    table[r][c].textSize = 11.5F
                                }

                            }
                        }
                        val row = slotMap["row"]!!.get(0)
                        val column = slotMap["column"]
                        for(c in column!!){
                            val currentText = table[row][c].text.toString()
                            if (currentText != "-"){
                                table[row][c].text = String.format("%s\n%s %s", currentText, courseName, section)
                                table[row][c].setBackgroundResource(R.drawable.table_box_red)
                            }
                            else{
                                table[row][c].text = String.format("%s %s", courseName, section)
                                table[row][c].setBackgroundResource(R.drawable.table_box_green)
                            }
                        }
                    }
                }
            //}
        }
    }
}