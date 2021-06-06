using System;
using System.Collections.Generic;
using System.Text;

namespace Lab_1
{
    class VigenereCipher : ICiperable
    {
        char[] key = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        public VigenereCipher()
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
            var k = key[0..5];
            for (int i = 0; i < input.Length; i++)
            {
                output.Append((char)(65 + (input[i] - k[i % 5] + 25) % 26));
            }

            return output.ToString();
        }

        public string Encrypt(string input)
        {
            var output = new StringBuilder();
            var k = key[0..5];
            for (int i = 0; i < input.Length; i++) 
            {
                output.Append((char)(65 + (input[i] + k[i % 5] - 129) % 26));
            }

            return output.ToString();
        }

        public void PrintKey()
        {
            Console.WriteLine("Ключ  - {0}", new string(key[0..5]));
        }
    }
}
