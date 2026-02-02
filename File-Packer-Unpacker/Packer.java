//////////////////////////////////////////////////////////////////////////////////////////////////
//
// Program Name : File Packing Utility
// Description  : This program packs all (.txt) files from a given directory into a single packed 
//                file.It uses fixed-size headers (100 bytes) and XOR encryption for data security.
// Author       : Saurabh Santosh Kanade
// Language     : Java
// Date         : 25/01/2026
//
///////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////////////////
//
// Class Name    : Packer
// Description   : Accepts folder name and packed file name
//                 from user and performs packing operation.
//
///////////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////////////////////
//
// Function Name : main
// Description   : Entry point of application. Reads all .txt files from folder, creates header,
//                 encrypts data and stores it in packed file.
// Input         : Command line arguments
// Output        : Single packed file
//
/////////////////////////////////////////////////////////////////////////////////////////////////
class Packer
{
    public static void main(String A[]) throws Exception
    {
        // ---------------- Variable Declaration ----------------
        String Header = null;

        byte Key = 0x11;            // XOR encryption key

        int iRet = 0;
        int i = 0, j = 0;

        byte Buffer[] = new byte[1024];   // Buffer for file data
        byte bHeader[] = new byte[100];   // Fixed-size header buffer

        Scanner sobj = new Scanner(System.in);

        // ---------------- Accept Input ----------------
        System.out.println("Enter the name of folder : ");
        String FolderName = sobj.nextLine();

        System.out.println("Enter the name of packed file : ");
        String PackName = sobj.nextLine();
        
        File fobj = new File(FolderName);

        // ---------------- Folder Validation ----------------
        if((fobj.exists()) && (fobj.isDirectory()))
        {
            File PackObj = new File(PackName);
            PackObj.createNewFile();

            FileOutputStream foobj = new FileOutputStream(PackObj);
            FileInputStream fiobj = null;

            System.out.println("Folder is present"); 

            File fArr[] = fobj.listFiles();
            System.out.println("Number of files in the folder are : " + fArr.length);

            // ---------------- Packing Logic ----------------
            for(i = 0; i < fArr.length; i++)
            {
                fiobj = new FileInputStream(fArr[i]);

                if(fArr[i].getName().endsWith(".txt"))
                {
                    // Header formation (FileName + FileSize)
                    Header = fArr[i].getName() + " " + fArr[i].length();

                    // Padding header to 100 bytes
                    for(j = Header.length(); j < 100; j++)
                    {
                        Header = Header + " ";
                    }
                    
                    bHeader = Header.getBytes();

                    // Write header into packed file
                    foobj.write(bHeader, 0, 100);
                    
                    // Read file data, encrypt and write
                    while((iRet = fiobj.read(Buffer)) != -1)
                    {
                        // Encryption logic (XOR)
                        for(j = 0; j < iRet ; j++)
                        {
                            Buffer[j] = (byte)(Buffer[j] ^ Key);
                        }
                        
                        // Write encrypted data
                        foobj.write(Buffer, 0, iRet);
                    }
                }
                fiobj.close();
            }
            foobj.close();
            System.out.println("Packing completed successfully");
        }
        else
        {
            System.out.println("There is no such folder");
        }
    }
}
