package com.example.littlechemist3


import java.util.*
import kotlin.math.abs

class Node(text:String)
{

    var Text = text.uppercase(Locale.getDefault())
    var Id:Int = 0
        //get() {            TODO()        }

    var MaxNodes:Int  = 1
    val Nodes =  mutableListOf<Node>()
    private var BondCount:Double = 0.0  // double for special cases, like NO2

    init
    {
        SetMaxNodes(Text)
        Id = currentId
        currentId++
    }

    /**
     * How many connections are possible.
     */
    private fun SetMaxNodes( text:String)
    {
        //TODO: use negative values too, as in chemistry, e.g. "O" = -2. Abs value already used in math below
        MaxNodes = when(text)  {    //        switch (text)
            "H" -> 1// break;
            "OH" -> 1
            "CN" -> 1  // TODO: implement cyanide
            "C" -> 4
            "O" -> 2
            "N" -> 3
            "Ca" -> 2
            else -> 0
        }
    }

    /// <summary>
    /// Add link, if link count not full
    /// </summary>
    /// <param name="n">Node to link</param>
    /// <returns>Number of free node slots left</returns>
    fun AddLink( n:Node):Double
    {
        val bondsPlus = CheckSpecialBond(Text, n.Text)  // e.g. C-O uses 2x bonds
        //if (Nodes.Count < MaxNodes)
        if ((bondsPlus + BondCount) <= abs(MaxNodes)) { // was Nodes.Count
            if (!n.Nodes.contains(this)) {
                n.Nodes.add(this)
                n.AddBonds(bondsPlus)
            }
            Nodes.add(n)
            BondCount += bondsPlus
        }
        else { println("No more items can be added to $Text (max. ${abs(MaxNodes)}") }

        //TODO: maybe same
        return if (IsFull()) { // was n.IsFull
            FreeBonds()
        } else {  abs(MaxNodes.toDouble()) - BondCount // Nodes.Count;
        }
    }

    private fun  AddBonds(n:Double)
    {
        when {
            n.toInt() in 1..4 -> { // so far 4+ bonds not supported
                BondCount += n
            }
        }
    }

    fun IsFull():Boolean
    {
        //checks also nodes with 2+ bonds
        var cnt = 0.0
        //for ( n in Nodes) { cnt += CheckSpecialBond(Text, n.Text) }
        Nodes.forEach { cnt += CheckSpecialBond(Text, it.Text) }
        return abs(MaxNodes.toDouble()) - cnt < 1.0  //cnt == MaxNodes.toDouble();
    }

    private fun FreeBonds():Double
    {
        //checks also nodes with 2+ bonds
        val cnt = Nodes.sumOf { CheckSpecialBond(Text, it.Text) }
        return abs(MaxNodes.toDouble()) - cnt  //cnt == MaxNodes.toDouble();
    }


    override fun toString():String
    {
        return "$Id: $Text ($Nodes.Count) "
    }

    companion object {
        private var  currentId:Int = 1
        fun Reset() {
            currentId = 1
        }

        /**
        * Double etc bonding. Param order doesn't matter. Note: not same as max nodes
        * @param from  First node text
        * @param to Second node text
        * @returns Bond count, default 1
         * */
        fun CheckSpecialBond(from:String, to:String):Double
        {
            when {
                (from == "C" || to == "C") && from != to -> {
                    //if ((from == "C" && to == "O") || (from == "O" && to == "C"))
                    if (to == "O" || from == "O") { return 2.0  }
                    if (to == "N" || from == "N") { return 3.0  }
                }
            }

            //TODO: how about others than HNO3
            when {
                (from == "N" || to == "N") && from != to -> {
                    if (to == "O" || from == "O") { return 1.5 }
                }
            }
            return 1.0
        }
    }
}

