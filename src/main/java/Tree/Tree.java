/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tree;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Colby Morrissey
 * Class that handles the search tree for the UPC.
 * It can build the search tree, and perform BFS and DFS for a given UPC.
 */
public class Tree {
    public Node root;
    private List<Node> closedList = new LinkedList<>();
    public static final String upcArray = "upcArray";
    public static final String productDescriptionArray = "descriptionArray";

    /**
     * Class to store all of the data for a given Node.
     */
     public static class Node {
         String data;
         String description;
         Node left, right;
         boolean visited = false;

         Node(String data, String description){
             this.data = data;
             this.description = description;
             this.left = null;
             this.right = null;
         }
   
    }

    /**
     * Get the nodes visited (closed list) as a List<String>
     * for the search.
     * @return List<String> representing the closed list for a given search.
     */
    public List<String> getClosedList(){
        List<String> copyClosedList = new ArrayList<>();
        for (Node node : closedList) {
            copyClosedList.add(node.data);
        }
        return copyClosedList;
    }

    /**
     * Build the current Tree, recursively.
     * @param upc array containing all of the UPC's for the search tree
     * @param description array containing all of the product descriptions for the search tree
     * @param root root node for the tree
     * @param i base case for i
     * @return root node of the newly built tree
     */
     public Node insertLevelOrder(String[] upc, String[] description, Node root, int i)
    {
        // Base case for recursion 
        
        if (i < upc.length) {
            root = new Node(upc[i], description[i]);
  
            // insert left child 
            root.left = insertLevelOrder(upc, description, root.left,
                                             2 * i + 1); 
  
            // insert right child 
            root.right = insertLevelOrder(upc, description, root.right,
                                               2 * i + 2); 
        }
       
       return root; 
    }

    /**
     * Method to reset all of the nodes 'visited' value
     * to false after a search.
     * @param root root node of the tree to reset.
     */
    public void reset(Node root)
    { 
        if (root != null) { 
            reset(root.left);
            root.visited = false;
            //System.out.println(root.data + " " + root.descrip); 
            reset(root.right); 
        } 
    }

    /**
     * Method to load two arrays, one for the UPC's in the given CSV file and another for the
     * description. Will return a map, Map<String, String[]>, with the upc array and product description array.
     * @param filepath The file path to the CSV to use for the UPC's and product descriptions.
     * @return Map<String, String[]>, with the upc array and product description array. Keys are Tree.upcArray and Tree.productDescriptionArray.
     * @throws IOException if the filepath given is not valid
     */
    public Map<String, String[]> loadArray(String filepath) throws IOException{
        Map<String, String[]> mapArrays = new HashMap<>();
        try{
            Reader in = new FileReader(filepath);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            String[] arrayUPC = new String[100];
            String[] arrayDescrip = new String[100];
        
            int i = 0;
            for (CSVRecord record : records) {
                arrayUPC[i] = record.get(0);  
                arrayDescrip[i] = record.get(1);  
                i++;
            }
            mapArrays.put(upcArray, arrayUPC);
            mapArrays.put(productDescriptionArray, arrayDescrip);
        }
        catch(IOException e){
            System.out.println("An Error Occurred reading the csv!");
            System.out.println(e.getMessage());
        } 
        
        return mapArrays;
    }

    /**
     * Search the tree using the BFS algorithm, given the searchkey (UPC).
     * @param searchKey : UPC to search for, must be 5 digits.
     * @return Product description, if the product was able to be found.
     */
    public String bfs(String searchKey) {

        String prodDescrip = "Not Found! This product is not in the search tree.";
        Queue<Node> openList = new LinkedList<>();
        closedList = new LinkedList<>();
        openList.add(root);

        while (!openList.isEmpty())  
        { 
            Node tempNode = openList.poll();
            if(tempNode.data.equals(searchKey)){
                prodDescrip = tempNode.description;
                break;
            }
            else{
                
                
                //Enqueue left child
                if (tempNode.left != null && !tempNode.left.visited) { 
                    openList.add(tempNode.left);
                }
            /*Enqueue right child */
                if (tempNode.right != null && !tempNode.right.visited) { 
                    openList.add(tempNode.right);
                //System.out.print(" " + tempNode.right.data);
                }
                //add current node to closed list, so long as it has not been visited
                if (!tempNode.visited){
                   closedList.add(tempNode); 
                }
                //Mark node as visited
                tempNode.visited = true;

            }
        }
      
        return prodDescrip;

    }
    /**
     * Search the tree using the DFS algorithm, given the searchkey (UPC). It will
     * traverse the Left side of the tree first (preorder, left node preferable)
     * @param searchKey : UPC to search for, must be 5 digits.
     * @return Product description, if the product was able to be found.
     */
    public String dfs(String searchKey) {
        //preorder, prefer left node
        String prodDescrip = "Not Found! This product is not in the search tree.";
        Stack<Node> openList = new Stack<>();
        closedList = new LinkedList<>();
        openList.add(root);

        while (!openList.isEmpty())  
        { 
            Node tempNode = openList.pop();
            if(tempNode.data.equals(searchKey)){
                prodDescrip = tempNode.description;
                break;
            }
            else{
                
             /*Stack right child */
                if (tempNode.right != null && !tempNode.right.visited) { 
                    openList.add(tempNode.right);
                //System.out.print(" " + tempNode.right.data);
                }               
                //Stack left child
                if (tempNode.left != null && !tempNode.left.visited) { 
                    openList.add(tempNode.left);
                }

                //add current node to closed list, so long as it has not been visited
                if (!tempNode.visited){
                   closedList.add(tempNode); 
                }
                //Mark node as visited
                tempNode.visited = true;

            }
        }
      
        return prodDescrip;

    }
}

