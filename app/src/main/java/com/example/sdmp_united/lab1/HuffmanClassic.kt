package com.example.sdmp_united.lab1

import java.lang.StringBuilder
import kotlin.math.min

data class HuffmanClassic(val text: String) {

    var encodedText = ""
    var tableCodes = ArrayList<Pair<String, String>>()

    fun createCodes(list : ArrayList<Pair<String, String>>) : ArrayList<Pair<String, String>>
    {
        val tree = ArrayList<Node>()
        for (el in list)
        {
            val node = Node(el.first, el.second.toInt(), null, null)
            tree.add(node)
        }
        val sortedTree = ArrayList(tree.sortedWith(compareBy { it.frequency }))
        val top = createTree(sortedTree)
        findCodes(top)
        return tableCodes
    }

    fun encode() : String
    {
        val res = StringBuilder()
        for (ch in text)
        {
            val tmp = tableCodes.find { it.first == ch.toString() }?.second
            res.append(tmp)
        }
        encodedText = res.toString()
        return res.toString()
    }

    fun decode() : String
    {
        val tmpText = StringBuilder(encodedText)
        val res = StringBuilder()
        while (tmpText.isNotEmpty())
        {
            for (item in tableCodes)
            {
                if (item.second == tmpText.substring(0, min(tmpText.length, item.second.length)))
                {
                    tmpText.delete(0, item.second.length)
                    res.append(item.first)
                }
            }

        }
        return res.toString()
    }

    private fun printTree(list: ArrayList<Node>)
    {
        for (x in list)
        {
            print("[" + x.text + " : " + x.frequency + "]  ")
        }
        println()
    }

    private fun createTree(tree : ArrayList<Node>) : Node
    {
        if (tree.size > 1)
        {
            val newNode = Node(tree[0].text + tree[1].text, tree[0].frequency + tree[1].frequency, tree[0], tree[1])
            tree.removeAt(0)
            tree.removeAt(0)
            tree.add(newNode)
            val newTree = ArrayList(tree.sortedWith(compareBy { it.frequency }))
            return createTree(newTree)
        }
        return tree[0]
    }

    private fun findCodes(top : Node)
    {
        if (top.left != null)
        {
            top.left.code = top.code + "0"
            findCodes(top.left)
        }
        if (top.right != null)
        {
            top.right.code = top.code + "1"
            findCodes(top.right)
        }
        if (top.left == null && top.right == null)
            tableCodes.add( Pair(top.text, top.code))
    }

    fun getUniqueChars() : ArrayList<Pair<String, String>>
    {
        val str = text

        val list = ArrayList<Char>()
        for (ch in str)
        {
            if (!list.contains(ch))
                list.add(ch)
        }
        val res = ArrayList<Pair<String, String>>()
        for (el in list)
        {
            res.add( Pair(el.toString() , countMatches(str, el.toString())) )
        }
        return res
    }

    private fun countMatches(text: String, template: String): String {
        var cnt = 0
        var pos = 0
        while (true) {
            pos = text.indexOf(template, pos)
            if (pos != -1) {
                cnt++
                pos++
            } else {
                return cnt.toString()
            }
        }
    }

    private class Node(val text: String, val frequency : Int, val left : Node?, val right : Node?)
    {
        var code = ""
    }
}