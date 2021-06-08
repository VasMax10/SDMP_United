package com.example.sdmp_united.lab1

import java.lang.StringBuilder
import kotlin.math.min

data class HuffmanMod(val text: String) {

    var encodedText = ""
    var tableCodes = ArrayList<Pair<String, String>>()
    var lt = ArrayList<Node>()

    fun createCodes(list : ArrayList<Pair<String, String>>) : ArrayList<Pair<String, String>>
    {
        val tree = ArrayList<Node>()
        for (el in list)
        {
            val node = Node(el.first, el.second.toInt(), null, null)
            tree.add(node)
        }
        lt = tree
        combineChars()
        val top = createTree(lt)
        findCodes(top)
        return tableCodes
    }

    fun encode() : String
    {
        val res = StringBuilder()
        var txt = text

        while (txt.isNotEmpty())
            for (code in tableCodes)
            {
                val pos = txt.indexOf(code.first)
                if (pos == 0)
                {
                    txt = txt.removeRange(0, code.first.length)
                    res.append(code.second)
                }
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
        val sorted = ArrayList(res.sortedWith(compareBy { it.second.toInt() }))
        return sorted
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


    fun combineChars()
    {
        var k = false
        val n = lt.size
        lt = ArrayList(lt.sortedWith(compareBy { it.frequency }))
        for (i in 0 until n)
        {
            for (j in i until n)
            {
                if (lt[i].frequency == lt[j].frequency)
                {
                    val tmp1 = lt[i].text + lt[j].text
                    val cnt1 = countMatches(text, tmp1)

                    if (cnt1 == lt[i].frequency.toString())
                    {
                        lt[i].text = tmp1
                        lt.removeAt(j)
                        k = true
                        break
                    }

                    val tmp2 = lt[j].text + lt[i].text
                    val cnt2 = countMatches(text, tmp2)
                    if (cnt2 == lt[i].frequency.toString())
                    {
                        lt[i].text = tmp2
                        lt.removeAt(j)
                        k = true
                        break
                    }
                }
                else
                    break
                if (k)
                    break
            }
            if (k)
                break
        }
        if (k)
            combineChars()
//        for (i in lt.indices)
//            println(lt[i].text + " " + lt[i].frequency + " " + i)
    }

    class Node(var text: String, val frequency : Int, val left : Node?, val right : Node?)
    {
        var code = ""
    }
}