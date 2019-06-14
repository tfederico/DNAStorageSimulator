package com.federico.target_tracker.utils;

import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCode {
    // input is an array of frequencies, indexed by character code
    public HuffmanTree buildTree(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        if (charFreqs.length % 2 == 0)
            trees.offer(new HuffmanLeaf(0, '平'));


        for (int i = 0; i < charFreqs.length; i++)
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char) i));

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 2) {
            // three trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
            HuffmanTree c = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b, c));
        }
        return trees.poll();
    }

    public void printCodes(HuffmanTree tree, StringBuffer prefix, HashMap map) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            // print out character, frequency, and code for this leaf (which is just the prefix)
            //System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
            map.putIfAbsent(leaf.value, prefix.toString());

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix, map);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse center
            prefix.append('1');
            printCodes(node.center, prefix, map);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('2');
            printCodes(node.right, prefix, map);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public abstract class HuffmanTree implements Comparable<HuffmanTree> {
        final int frequency; // the frequency of this tree

        HuffmanTree(int freq) {
            frequency = freq;
        }

        // compares on the frequency
        public int compareTo(HuffmanTree tree) {
            return frequency - tree.frequency;
        }
    }

    class HuffmanLeaf extends HuffmanTree {
        public final char value; // the character this leaf represents

        HuffmanLeaf(int freq, char val) {
            super(freq);
            value = val;
        }
    }

    class HuffmanNode extends HuffmanTree {
        final HuffmanTree left, center, right; // subtrees

        HuffmanNode(HuffmanTree l, HuffmanTree c, HuffmanTree r) {
            super(l.frequency + c.frequency + r.frequency);
            left = l;
            center = c;
            right = r;
        }
    }
}