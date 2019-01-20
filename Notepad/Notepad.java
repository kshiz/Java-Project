import java.io.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class Notepad extends Frame {
    Clipboard cBoard = getToolkit().getSystemClipboard();
    TextArea tArea;
    String fName;

    Notepad() {
        gaListener gListen = new gaListener();
        addWindowListener(gListen);
        tArea = new TextArea();
        add(tArea);
        MenuBar mBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem nOption = new MenuItem("New");
        MenuItem oOption = new MenuItem("Open");
        MenuItem sOption = new MenuItem("Save");
        MenuItem cOption = new MenuItem("Close");
        nOption.addActionListener(new Ne_option());
        fileMenu.add(nOption);
        oOption.addActionListener(new Ope_option());
        fileMenu.add(oOption);
        sOption.addActionListener(new Sav_option());
        fileMenu.add(sOption);
        cOption.addActionListener(new Clos_option());
        fileMenu.add(cOption);
        mBar.add(fileMenu);
        Menu editMenu = new Menu("Edit");
        MenuItem cutOption = new MenuItem("Cut");
        MenuItem copyOption = new MenuItem("Copy");
        MenuItem pasteOption = new MenuItem("Paste");
        cutOption.addActionListener(new Cu_option());
        editMenu.add(cutOption);
        copyOption.addActionListener(new Cop_option());
        editMenu.add(copyOption);
        pasteOption.addActionListener(new Past_option());
        editMenu.add(pasteOption);
        mBar.add(editMenu);
        setMenuBar(mBar);
        setTitle("Notepad");
    }

    class gaListener extends WindowAdapter {
        public void windowClosing(WindowEvent closeNotepad) {
            System.exit(0);
        }
    }

    class Ne_option implements ActionListener {
        public void actionPerformed(ActionEvent ne) {
            tArea.setText(" ");
        }
    }

    class Ope_option implements ActionListener {
        public void actionPerformed(ActionEvent ope) {
            // FileDialog fDialog=new FileDialog(Notepad.this,"Select a text
            // file",FileDialog.LOAD);
            // fDialog.show();
            // if (fDialog.getFile()!=null){
            // fName=fDialog.getDirectory()+fDialog.getFile();
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Java Source Code", "java"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION)
                fName = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println(fName);

            String[] tokens = fName.split("//");

            setTitle(tokens[tokens.length-1]);
            ReadFile();
            // }
            tArea.requestFocus();
        }
    }

    class Clos_option implements ActionListener {
        public void actionPerformed(ActionEvent close_o) {
            System.exit(0);
        }
    }

    class Sav_option implements ActionListener {
      public void actionPerformed(ActionEvent sav_o){
          
          JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
          fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Java Source Code","java"));
          String sName="";
          int result = fileChooser.showSaveDialog(null);
          if(result == JFileChooser.APPROVE_OPTION)
            {   
            fName = fileChooser.getSelectedFile().getAbsolutePath();
            String[] tokenList = fName.split("//");
             sName = tokenList[tokenList.length-1];
            }

        //fName = setExtension();

           System.out.println(sName);

            setTitle(sName);
            try{
            DataOutputStream dOutStream=new DataOutputStream(new FileOutputStream(sName));
                String oLine=tArea.getText();
                BufferedReader br =new BufferedReader(new StringReader(oLine));
                while((oLine=br.readLine())!=null){
                        dOutStream.writeBytes(oLine+"\r\n");
                }
                dOutStream.close();
            }
            catch(Exception ex){
                System.out.println("File not found");
            }
            tArea.requestFocus();
        }

        /*
         * private String setExtension(){
         * 
         * //Statements
         * 
         * }
         */
    }

    void ReadFile() {
        BufferedReader br;
        StringBuffer s = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(fName));
            String oLine;
            while ((oLine = br.readLine()) != null) {
                s.append(oLine + "\n");
                tArea.setText(s.toString());
            }
            br.close();
        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
        } catch (IOException ioe) {
        }
    }

    class Cu_option implements ActionListener {
        public void actionPerformed(ActionEvent cut_o) {
            String sText = tArea.getSelectedText();
            StringSelection sSelection = new StringSelection(sText);
            cBoard.setContents(sSelection, sSelection);
            tArea.replaceRange("", tArea.getSelectionStart(), tArea.getSelectionEnd());
        }
    }

    class Cop_option implements ActionListener {
        public void actionPerformed(ActionEvent copy_o) {
            String sText = tArea.getSelectedText();
            StringSelection cString = new StringSelection(sText);
            cBoard.setContents(cString, cString);
        }
    }

    class Past_option implements ActionListener {
        public void actionPerformed(ActionEvent paste_o) {
            Transferable cTransfer = cBoard.getContents(Notepad.this);
            try {
                String sText = (String) cTransfer.getTransferData(DataFlavor.stringFlavor);
                tArea.replaceRange(sText, tArea.getSelectionStart(), tArea.getSelectionEnd());
            } catch (Exception exc) {
                System.out.println("Not a string Flavor");
            }
        }
    }

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {

            System.out.println(ex);

        }
        Frame nFrame = new Notepad();
        nFrame.setSize(600, 600);
        nFrame.setVisible(true);
    }
}