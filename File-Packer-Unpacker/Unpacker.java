//////////////////////////////////////////////////////////////////////////////////////
//
// Program Name : File Unpacking Utility
// Description  : This program unpacks files from a single packed file. It reads 
//                fixed-size headers,decrypts data using XOR encryption and
//                restores original files.
// Author       : Saurabh Santosh Kanade
// Language     : Java
// Date         : 26/02/2026
//
/////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////////
//
// Class Name    : Unpacker
// Description   : Accepts packed file name from user and performs unpacking operation.
//
//////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////
//
// Function Name : main
// Description   : Entry point of application. Reads header and encrypted data from 
//                 packed file, decrypts it and recreates original files.
// Input         : Command line arguments
// Output        : Extracted files
//
//////////////////////////////////////////////////////////////////////////////////////
class Unpacker
{
    public static void main(String A[]) throws Exception
    {
        // ---------------- Variable Declaration ----------------
        int FileSize = 0;     // Size of file to be extracted
        int i = 0;            // Loop counter
        int iRet = 0;         // Bytes read

        byte Key = 0x11;      // XOR decryption key

        Scanner sobj = null;
        String FileName = null;   // Packed file name
        String Header = null;     // Header information
        String Tokens[] = null;   // Tokenized header values

        File fpackobj = null;     // Packed file object
        File fobj = null;         // Extracted file object
    
        FileInputStream fiobj = null;   // Input stream for packed file
        FileOutputStream foobj = null;  // Output stream for extracted file
    
        byte bHeader[] = new byte[100]; // Fixed-size header buffer
        byte Buffer[] = null;           // Buffer for file data

        // ---------------- Accept Input ----------------
        sobj = new Scanner(System.in);
        
        System.out.println("Enter the name of packed file : ");
        FileName = sobj.nextLine();

        fpackobj = new File(FileName);

        // ---------------- Packed File Validation ----------------
        if(fpackobj.exists() == false)
        {
            System.out.println("Error : There is no such packed file");
            return;
        }
        
        // Open packed file for reading
        fiobj = new FileInputStream(fpackobj);

        // ---------------- Unpacking Logic ----------------
        while((iRet = fiobj.read(bHeader, 0, 100)) != -1)
        {
            // Convert header bytes to string
            Header = new String(bHeader);

            // Remove extra spaces
            Header = Header.trim();

            // Split header to get file name and file size
            Tokens = Header.split(" ");

            System.out.println("File name : " + Tokens[0]);
            System.out.println("File size : " + Tokens[1]);
            
            // Create output file
            fobj = new File(Tokens[0]);
            fobj.createNewFile();

            foobj = new FileOutputStream(fobj);

            // Convert file size from string to integer
            FileSize = Integer.parseInt(Tokens[1]);

            // Allocate buffer for encrypted data
            Buffer = new byte[FileSize];

            // Read encrypted data from packed file
            fiobj.read(Buffer, 0, FileSize);

            // Decrypt data using XOR operation
            for(i = 0; i < FileSize ; i++)
            {
                Buffer[i] = (byte)(Buffer[i] ^ Key);
            }

            // Write decrypted data into extracted file
            foobj.write(Buffer, 0, FileSize);

            // Close extracted file
            foobj.close();
        }

        // Close packed file
        fiobj.close();

        System.out.println("Unpacking completed successfully");
    }
}
