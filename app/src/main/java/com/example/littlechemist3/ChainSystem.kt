package com.example.littlechemist3

//TODO: Hydrogen autofill when new >1 link node is selected

/** Main data structure of application
 * @param
 */
class ChainSystem/*(knownData: String)*/ {
    companion object {
        private val Chain = mutableListOf<Node>()
        private var Known: /*Mutable*/Map<String, String>
        private var knownData: String=""

        init {
            Known = parseKnown(knownData)
        }

        private lateinit var previous: Node // TODO: bad

        /**
         * TODO: sort node text for simpler matching
         * @param s
         */
        private fun ParseNodeText(s: String): String {
            //TODO: parsenodetext
            return s
        }
/** Set Known Data
* @param data - csv file contents of known molecule descriptions
* */
        fun setKnownData(data: String) {
            Known = parseKnown(data)//knownData = data
        }

        /**
         * Create a map from string data
         * @param knownData each line contains 2 fields
         * @return map where key = 1st field, value = 2nd field
         * Use associate for creating a map from a list: In your parseKnown function, you're using a loop to create a map from a list.
         * You can use associate function for this purpose which is more idiomatic in Kotlin.
         */
        private fun parseKnown(knownData: String): MutableMap<String, String> {
            val retMap = mutableMapOf<String, String>()
            val lines = knownData.split("\n")
            //return lines.associate { it.split(";")[0] to it.split(";")[1]}
            lines.forEach {
                val fields = it.split(";")
                if (fields.size > 1) {
                    retMap[fields[0]] = fields[1]
                }
            }
            return retMap
        }

        /**
         * Clear Chain data
         */
        fun Clear() {
            Chain.clear()
        }

        /**
         * Check if Chain data is empty
         */
        fun isEmpty(): Boolean {
            return Chain.isEmpty()
        }


        /*
    public void Add(string s)  {
        var n = new Node(s);
        Chain.Add(n);
        previous = n;
    }*/
/*
* Use run for executing a block of code with a different context: In your Link function, you're executing a block of code with n as the context.
* You can use run function for this purpose which is more idiomatic in Kotlin.
* Use with for executing a block of code with a different context: In your Link function, you're executing a block of code with n as the context.
* You can use with function for this purpose which is more idiomatic in Kotlin.
Use let for executing a block of code with a different context: In your Link function, you're executing a block of code with n as the context.
* You can use let function for this purpose which is more idiomatic in Kotlin.
Use apply for executing a block of code with a different context: In your Link function, you're executing a block of code with n as the context.
* You can use apply function for this purpose which is more idiomatic in Kotlin.
Use also for executing a block of code with a different context: In your Link function, you're executing a block of code with n as the context.
* You can use also function for this purpose which is more idiomatic in Kotlin.
*
* Use takeIf for conditional assignments: In your Link function, you're assigning previous conditionally.
* You can use takeIf function for this purpose which is more idiomatic in Kotlin.
Use takeUnless for conditional assignments: In your Link function, you're assigning previous conditionally.
* You can use takeUnless function for this purpose which is more idiomatic in Kotlin.


Use runCatching for handling exceptions: In your Link function, you're catching an exception. You can use runCatching function for this purpose which is more idiomatic in Kotlin.
Use getOrElse for handling exceptions: In your Link function, you're catching an exception. You can use getOrElse function for this purpose which is more idiomatic in Kotlin.
Use getOrNull for handling exceptions: In your Link function, you're catching an exception. You can use getOrNull function for this purpose which is more idiomatic in Kotlin.
Use getOrDefault for handling exceptions: In your Link function, you're catching an exception. You can use getOrDefault function for this purpose which is more idiomatic in Kotlin.
* */
        /** Link and add
         ** @param s - new node text **/
        fun Link(s: String): Node {
            val n = Node(s)
            var free: Double = -1.0 // 0 means no mode left, -1 = first in chain

            if (Chain.isNotEmpty()){//Chain.count() > 0) {
                free = previous.AddLink(n)
            } else {
                previous = n
            }

            // TODO: find way to get this correct every time, maybe one with biggest maxnodes?
            if (n.MaxNodes > 1 && free == 0.0) {
                previous = n
            }

            /* not used, using OH  type instead
        // O+H -> OH when last
        if(false)//Chain.Count > 2 && IsComplete() && previous.Text == "O" && n.Text == "H") {
            // TODO: CH3OH -> C still has extra O (changes to extra OH)
            // Replace O with OH, don't add H
            Node ohnode = new("OH");
            // previous = "O"
            if (previous?.Nodes.Count > 0) {
                ohnode.AddLink(previous?.Nodes[0]); // Nodes[0] links back
                //TODO: fix
                var index = previous.Nodes[0].Nodes.FindIndex(n => n.Id == ohnode.Id);
                if(index >= 0) { previous.Nodes[0].Nodes[index] = ohnode; }
                //if (!previous?.Nodes[0].Nodes.Contains(ohnode))
                //{
                //previous?.Nodes[0].Nodes.Add(ohnode);
                //}
                // replace node when someone links to "O"
                var masternode = FindNode(previous.Id);
                var index2 = masternode.Nodes.FindIndex(n=> n.Id ==previous.Id);
                masternode.Nodes[index2] = ohnode;
            }
            Chain[^1] = ohnode;

        }
        else
        {
        */
            Chain.add(n)
            //}
            return n
        }

        /** Check, which node has link to specified id
         * @param id Id to search in nodes links
         * @returns Node, null otherwise **/
        private fun FindNode(id: Int): Node? {
            var retval: Node?

            Chain.forEach { item ->
                retval = item.Nodes.find { it.Id == id }
                if (retval != null) return item
            }
            return null
        }

        /** toString
         * @return formula as text as H2O
         **/
        override fun toString(): String {
            var s = ""
/*Use joinToString for concatenating strings: In your toString function, you're concatenating strings in a loop.
You can use joinToString function for this purpose which is more idiomatic in Kotlin.*/
            //foreach (var n in Chain)
            Chain.indices.forEach { i ->
                val n: Node = Chain[i]
                s += n.Nodes.fold("$i. $n.Text (") { acc, node ->
                    "$acc${node.Text} "
                }
                /* s += "$i. $n.Text ("
                        for (l in n.Nodes) { s += "$l.Text " }*/

                s += ") "
            }
            return s
        }

        // how to print via links

        /** Link to selected item
         * @param sel Item index
         * @param v New linked item text**/
        fun Link(sel: Int, v: String) {
            if (sel < Chain.count()) {
                previous = Chain[sel]
                Link(v)
            }
        }

        /** Print like H2O
         * @param formatted - true for pretty print
         * @returns formatted string **/
        fun toString(formatted: Boolean): String// = false)
        {
            //var s = "";
            when {
                formatted -> {
                    /*
                    val counts = mutableMapOf<String, Int>()
                    //numbersMap.put("three", 3)
                    //numbersMap["one"] = 11
                    //val counts = dictionaryOf<string, int>();

                    // count
                    for (n in Chain) {
                        if (!counts.containsKey(n.Text)) { counts[n.Text] = 1;
                        } else {  counts[n.Text] = counts[n.Text]!!.plus(1);   }
                    }

                    for (item in counts) {
                        s += if (item.value > 1) { "${item.key}${item.value}";
                        } else { item.key; }
                    }*/

                    var s = countElements()
                    s = MatchKnown(s)//, counts);
                    if (IsComplete()) {
                        s += " READY "
                    }
                    return s
                }
                else -> {
                    return toString()
                }
            }
        }

        /**
         * Count multiple equal symbols and return converted formula text
         * as HHH -> H3
         * @returns converted formula text
         * Use reduce or fold for aggregations: In your countElements function, you're using a loop to calculate a sum.
         * You can use reduce or fold function for this purpose which is more idiomatic in Kotlin.
         * Use groupBy and mapValues for grouping and counting: In your countElements function, you're using a loop to group and count elements.
         * You can use groupBy and mapValues functions for this purpose which are more idiomatic in Kotlin.
         */
        private fun countElements(): String {
            val counts = mutableMapOf<String, Int>()
            //numbersMap.put("three", 3)
            //numbersMap["one"] = 11

            //val counts = dictionaryOf<string, int>();

            // count
            Chain.forEach { n ->
                (if (!counts.containsKey(n.Text)) {
                    1   //counts[n.Text] = 1
                } else {
                    //counts[n.Text] =
                    counts[n.Text]!!.plus(1)
                }).also { counts[n.Text] = it }
            }

            //TODO:
            val map = counts.toSortedMap()

            var s = ""
            for ((key, value) in counts) {
                s += if (value > 1) {
                    "${key}${value}"
                } else {
                    key
                }
            }
            return s
        }

        /**
         * Is every possible bond used
         * @returns true / false
         * Use any or all for checking conditions on all elements: In your IsComplete function, you're using a loop to check if all elements satisfy a condition.
         * You can use any or all function for this purpose which is more idiomatic in Kotlin.
         */
        fun IsComplete(): Boolean {
            var retval = true
            Chain.forEach { retval = (retval and it.IsFull()) }
            //Chain.all { it.IsFull() }     // voisi korvata yo. rivin
            return retval
        }

        private val formulaItems = mutableListOf<FormulaItem>()

        private fun MatchKnown(s: String, counts: MutableMap<String, Int>): String {
            //for (item in counts) {

            counts.forEach {
                formulaItems.add(FormulaItem(it.key, it.value))  // was item
            }
            val list2 = formulaItems.sortedWith(compareBy { it.Code }) // not used?

            val ss = ParseNodeText(s)
            /*TODO: C2H5OH vs C2H6O
        // Replaced by list from net https://laitinent.gitihub.io/moleculelist.csv
        val v: String = when (ss) {
            "H2O" -> " (Vesi)"
            "OH2" -> "H2O (Vesi)"
            "CO2" -> "Hiilidioksidi"
            "CH2O" -> "Formaldehydi"
            "C2H6" -> "Etaani"
            "C2H5OH" -> "Etanoli"
            "C3H6O" -> "Asetoni"
            "CH4" -> "Metaani"
            "C3H8" -> "Propaani"  // lines from 2nd C->H mismatch
            //"CH3CH2CH3" => "Propaani",
            "CH3OH" -> "Metanoli"
            "NH3" -> "Ammoniakki"
            "NH2OH" -> "Hydroksyyliamiini"
            "CHN" -> "Syaanivety/ Sinihappo"
            "CNH" -> "Syaanivety/ Sinihappo"
            "CH3CN" -> "Asetonitriili"
            else -> s
        };
        return v;*/
            return if (Known.containsKey(ss)) {
                Known[ss]!!
            } else ""
        }

        /**
         * Does molecule string have a descriptive name
         * @return name if found, empty string otherwise
         */
        private fun MatchKnown(s: String): String {
            return if (Known.containsKey(s)) {
                Known[s]!!
            } else "?"
        }

        fun CountAndMatchKnown(): String {
            val s = countElements()
            return MatchKnown(s)
        }

        //data class FormulaItem(var Code: String, var Count: Int) //: IComparable
    }
}
data class FormulaItem(var Code: String, var Count: Int)


