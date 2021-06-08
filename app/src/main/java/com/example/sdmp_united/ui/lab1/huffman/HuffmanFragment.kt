package com.example.sdmp_united.ui.lab1.huffman

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.sdmp_united.R
import com.example.sdmp_united.lab1.HuffmanClassic
import com.example.sdmp_united.lab1.HuffmanMod
import com.example.sdmp_united.lab2.Communicator
//import com.example.sdmp_united.ui.lab1.histogram.HistogramFragment
import kotlinx.android.synthetic.main.dialog_check_huffman.*
import kotlinx.android.synthetic.main.dialog_codes.*
import kotlinx.android.synthetic.main.dialog_histogram.*
import kotlinx.android.synthetic.main.fragment_huffman.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HuffmanFragment : Fragment() {

    private lateinit var huffmanViewModel: HuffmanViewModel

    private lateinit var comm : Communicator


    var encodedText = ""
    var decodedText = ""
    var freq = ArrayList<Pair<String, String>>();
    var codes = ArrayList<Pair<String, String>>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        huffmanViewModel =
                ViewModelProvider(this).get(HuffmanViewModel::class.java)

        comm = requireActivity() as Communicator

        val root = inflater.inflate(R.layout.fragment_huffman, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deactivateButtons()

        button_clear_textbox.setOnClickListener{
            clearTextView()
        }

        button_check.setOnClickListener {
            showEncodedText()
        }

        button_classic_huffman.setOnClickListener{
            classicHuffman()
            activateButtons()
        }

        button_mod_huffman.setOnClickListener {
            modHuffman()
            activateButtons()
        }

        button_save.setOnClickListener {
            saveToFile()
        }

        button_read.setOnClickListener {
            openFile()
        }

        button_histogram.setOnClickListener {
            showChart()
        }
        button_codes.setOnClickListener {
            showCodesDialog()
        }

        button_frequencies.setOnClickListener {
            showFreqDialog()
        }

        button_check.setOnClickListener {
            showEncodedText()
        }

        textToEncode.addTextChangedListener {
            deactivateButtons()
        }

    }

    private fun activateButtons()
    {
        button_check.isEnabled = true
        button_check.isClickable = true

        button_frequencies.isEnabled = true
        button_frequencies.isClickable = true

        button_codes.isEnabled = true
        button_codes.isClickable = true

        button_histogram.isEnabled = true
        button_histogram.isClickable = true

        button_save.isEnabled = true
        button_save.isClickable = true
    }

    private fun deactivateButtons()
    {
        button_check.isEnabled = false
        button_check.isClickable = false

        button_frequencies.isEnabled = false
        button_frequencies.isClickable = false

        button_codes.isEnabled = false
        button_codes.isClickable = false

        button_histogram.isEnabled = false
        button_histogram.isClickable = false

        button_save.isEnabled = false
        button_save.isClickable = false
    }

    private fun classicHuffman() {
        val text = textToEncode.text.toString()

        try {
            val huf = HuffmanClassic(text)
            freq = huf.getUniqueChars()
            codes = huf.createCodes(freq)
            encodedText = huf.encode()
            decodedText = huf.decode()
            val toast = Toast.makeText(context, "Text was encoded", Toast.LENGTH_SHORT)
            toast.show()
        } catch (e: Exception) {
            val toast = Toast.makeText(context, "Text was NOT encoded", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun modHuffman() {
        try {
            val text = textToEncode.text.toString()
            val huf = HuffmanMod(text)
            freq = huf.getUniqueChars()
            codes = huf.createCodes(freq)
            encodedText = huf.encode()
            decodedText = huf.decode()
            if (encodedText == "")
            {
                classicHuffman()
            }
            else
            {
                val toast = Toast.makeText(context, "Text was encoded", Toast.LENGTH_SHORT)
                toast.show()
            }
        } catch (e: Exception) {
            val toast = Toast.makeText(context, "Text was NOT encoded", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun openFile () {
        val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file

            if (selectedFile != null) {
                val input = readTextFromUri(selectedFile)
                textToEncode.setText(input)
            }
        }
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        activity?.contentResolver?.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line + "\n")
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }

    private fun saveToFile() {
        val sdf = SimpleDateFormat("hh_mm_ss_dd_M")
        val currentDate = sdf.format(Date())
        val folderPath = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val folder = folderPath.toString() + "/"
        try
        {
            val file = File(folder, currentDate +"_EncodedText.txt")
            val writer = PrintWriter(file)
            writer.print(encodedText)
            writer.close()
            val toast = Toast.makeText(
                context,
                "File 'EncodedText.txt' successfully saved to " + folder,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
        catch (e: Exception)
        {
            val toast = Toast.makeText(
                context,
                "Error in saving 'Encoded Text'",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

        try
        {
            val file = File(folder, currentDate +"_Huffman's table_.txt")
            val writer = PrintWriter(file)
            for (x in codes)
            {
                writer.print(x.first + " " + x.second + "\n")
            }
            writer.close()
            val toast = Toast.makeText(
                context,
                "File 'Huffman's table.txt' successfully saved to " + folder,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
        catch (e: Exception)
        {
            val toast = Toast.makeText(
                context,
                "Error in saving 'Huffman's table'",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

    }

    private fun showChart() {

        val histogramDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        histogramDialog.setCancelable(true)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        histogramDialog.setContentView(R.layout.dialog_histogram)

        val charList = ArrayList<String>()
        val freqList = ArrayList<String>()

        for (i in freq.indices)
        {
            charList.add(freq[i].first)
            freqList.add(freq[i].second)
        }
//        comm.passDataHuffmanHistogram(charList, freqList)
        val anyChart = histogramDialog.any_chart_view
        val column = AnyChart.vertical()

        val dataEntries:ArrayList<DataEntry> = ArrayList()

        for (i in charList.indices)
        {
            if (charList[i] == "\n")
                dataEntries.add(ValueDataEntry("Enter", freqList[i].toInt()))
            else
                dataEntries.add(ValueDataEntry(charList[i], freqList[i].toInt()))
            println(charList[i] + " " + freqList[i].toInt())
        }
        println(dataEntries.size)
        column.data(dataEntries)
        anyChart.setChart(column)

        histogramDialog.show()

    }

    private fun clearTextView() {
        textToEncode.setText("")
    }

    fun openAbout(view: View) {
//        val intent = Intent(this,AboutActivity::class.java)
//        startActivity(intent)
    }

    private fun showCodesDialog() {
        val codesDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        codesDialog.setCancelable(true)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        codesDialog.setContentView(R.layout.dialog_codes)

        val listKeys = ArrayList<String>();
        val listValues = ArrayList<String>();

        for (x in codes)
        {
            listKeys.add(x.first)
            listValues.add(x.second)
        }


        val list = ArrayList<Pair<String, String>>()

        for (i in 0 until listKeys.size)
        {
            list.add(Pair(listKeys[i], listValues[i]))
        }

        val sortedList = list.sortedWith(compareByDescending { it.second.length } )

        val tableLayout = codesDialog.resTab;
        val tableRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

        var tableRow = TableRow(context)
        tableRow.layoutParams = tableRowParams

        var tv1 = TextView(context)
        tv1.text = "Symbol"
        var tv2 = TextView(context)
        tv2.text = "Code"
        var cellParams = TableRow.LayoutParams( 0, TableRow.LayoutParams.MATCH_PARENT)
        cellParams.weight = 1F
        tv1.gravity = Gravity.CENTER
        tv1.textSize = 22F
        tv1.setBackgroundColor(Color.DKGRAY)
        tv1.setTextColor(Color.WHITE)
        tv1.layoutParams = cellParams

        cellParams.weight = 2F
        tv2.gravity = Gravity.CENTER
        tv2.textSize = 22F
        tv2.setBackgroundColor(Color.DKGRAY)
        tv2.setTextColor(Color.WHITE)
        tv2.layoutParams = cellParams

        tableRow.addView(tv1)
        tableRow.addView(tv2)

        tableLayout.addView(tableRow)

        for (i in sortedList.indices) {

            tableRow = TableRow(context)
            tableRow.layoutParams = tableRowParams

            tv1 = TextView(context)
            tv1.text = sortedList[i].first
            tv2 = TextView(context)
            tv2.text = sortedList[i].second

            cellParams = TableRow.LayoutParams( 0, TableRow.LayoutParams.MATCH_PARENT)
            cellParams.weight = 1F
            tv1.gravity = Gravity.CENTER
            tv1.textSize = 22F
            tv1.layoutParams = cellParams

            tv2.gravity = Gravity.RIGHT
            tv2.textSize = 22F
            cellParams.weight = 2F
            tv2.layoutParams = cellParams

            if (i % 2 == 1)
            {
                tv1.setBackgroundColor(Color.GRAY)
                tv1.setTextColor(Color.BLACK)

                tv2.setBackgroundColor(Color.GRAY)
                tv2.setTextColor(Color.BLACK)
            }
            else
            {
                tv1.setBackgroundColor(Color.LTGRAY)
                tv1.setTextColor(Color.BLACK)

                tv2.setBackgroundColor(Color.LTGRAY)
                tv2.setTextColor(Color.BLACK)
            }

            tableRow.addView(tv1)
            tableRow.addView(tv2)

            tableLayout.addView(tableRow)
        }

        //Start the dialog and display it on screen.
        codesDialog.show()
    }

    private fun showFreqDialog () {
//        val tableIntent = Intent(this,TableResult::class.java)

        val codesDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        codesDialog.setCancelable(true)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        codesDialog.setContentView(R.layout.dialog_codes)


        val listKeys = ArrayList<String>();
        val listValues = ArrayList<String>();

        for (x in freq)
        {
            listKeys.add(x.first)
            listValues.add(x.second)
        }
        val list = ArrayList<Pair<String, String>>()

        for (i in 0 until listKeys.size)
        {
            list.add(Pair(listKeys[i], listValues[i]))
        }

        val sortedList = list.sortedWith(compareByDescending { it.second.length } )

        val tableLayout = codesDialog.resTab;
        val tableRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

        var tableRow = TableRow(context)
        tableRow.layoutParams = tableRowParams

        var tv1 = TextView(context)
        tv1.text = "Symbol"
        var tv2 = TextView(context)
        tv2.text = "Frequency"
        var cellParams = TableRow.LayoutParams( 0, TableRow.LayoutParams.MATCH_PARENT)
        cellParams.weight = 1F
        tv1.gravity = Gravity.CENTER
        tv1.textSize = 22F
        tv1.setBackgroundColor(Color.DKGRAY)
        tv1.setTextColor(Color.WHITE)
        tv1.layoutParams = cellParams

        cellParams.weight = 2F
        tv2.gravity = Gravity.CENTER
        tv2.textSize = 22F
        tv2.setBackgroundColor(Color.DKGRAY)
        tv2.setTextColor(Color.WHITE)
        tv2.layoutParams = cellParams

        tableRow.addView(tv1)
        tableRow.addView(tv2)

        tableLayout.addView(tableRow)

        for (i in sortedList.indices) {

            tableRow = TableRow(context)
            tableRow.layoutParams = tableRowParams

            tv1 = TextView(context)
            tv1.text = sortedList[i].first
            tv2 = TextView(context)
            tv2.text = sortedList[i].second

            cellParams = TableRow.LayoutParams( 0, TableRow.LayoutParams.MATCH_PARENT)
            cellParams.weight = 1F
            tv1.gravity = Gravity.CENTER
            tv1.textSize = 22F
            tv1.layoutParams = cellParams

            tv2.gravity = Gravity.RIGHT
            tv2.textSize = 22F
            cellParams.weight = 2F
            tv2.layoutParams = cellParams

            if (i % 2 == 1)
            {
                tv1.setBackgroundColor(Color.GRAY)
                tv1.setTextColor(Color.BLACK)

                tv2.setBackgroundColor(Color.GRAY)
                tv2.setTextColor(Color.BLACK)
            }
            else
            {
                tv1.setBackgroundColor(Color.LTGRAY)
                tv1.setTextColor(Color.BLACK)

                tv2.setBackgroundColor(Color.LTGRAY)
                tv2.setTextColor(Color.BLACK)
            }

            tableRow.addView(tv1)
            tableRow.addView(tv2)

            tableLayout.addView(tableRow)
        }

        //Start the dialog and display it on screen.
        codesDialog.show()
    }

    private fun showEncodedText () {
        val testDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        testDialog.setCancelable(true)

        testDialog.setContentView(R.layout.dialog_check_huffman)

        val inputText = textToEncode.text.toString()
        testDialog.viewInputText.setText(inputText)
        testDialog.viewDecodedText.setText(decodedText)
        testDialog.viewEncodedText.setText(encodedText)

        testDialog.checkButton.setOnClickListener {
            if (inputText == decodedText)
            {
                testDialog.textViewCheckInfo.text = "Correct"
                testDialog.checkButton.setBackgroundColor(Color.GREEN)
            }
            else
            {
                testDialog.textViewCheckInfo.text = "Incorrect"
                testDialog.checkButton.setBackgroundColor(Color.RED)
            }
        }
        //Start the dialog and display it on screen.
        testDialog.show()
    }
}
