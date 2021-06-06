using System;
using System.Collections.Generic;
using System.Text;

namespace Lab_1
{
    class DefaultCipher : ICiperable
    {
        char[] key = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
       public DefaultCipher()
        {
            key = GetRundomKey();
        }

        public char[] GetRundomKey()
        {
            var randIndex = new Random();
            for (int i = 0; i < 50; i++)
            {
                var index1 = randIndex.Next(0, 25);
                var index2 = randIndex.Next(0, 25);
                (key[index1], key[index2]) = (key[index2], key[index1]);
            }

            return key;
        }
        public string Decrypt(string input, string key)
        {
            var output = new StringBuilder();
            foreach (var ch in input)
            {
                output.Append((char)(key.IndexOf(ch) + 'A'));
            }

            return output.ToString();
        }

        public string Encrypt(string input)
        {
            var output = new StringBuilder();
            foreach (var ch in input) 
            {
                output.Append(key[ch - 'A']);
            }

            return output.ToString();
        }

        public void PrintKey()
        {
            for (int i = 'A'; i <= 'Z'; i++) Console.Write((char)i + " ");
            Console.WriteLine();
            for (int i = 0; i < 26; i++) Console.Write("| ");
            Console.WriteLine();
            foreach (var x in key) Console.Write(x + " ");
            Console.WriteLine();
            Console.WriteLine("Ключ  - {0}", new string(key));
        }
    }
}
