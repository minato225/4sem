using System;
using System.Collections.Generic;
using System.Text;

namespace Lab_1
{
    class AffineCipher : ICiperable
    {
        private Tuple<int, int> key;

        public AffineCipher()
        {
            SetRamdomKey();
        }
        public string Decrypt(string input, string key)
        {
            var a = key[0];
            var b = key[1];
            var A = A_INV(a - 'A');
            var output = new StringBuilder();
            foreach (var x in input)
            {
                var d = A * (x - b) % 26;
                d += d < 0 ? 26 : 0;
                output.Append((char)(d + 'A'));
            }
            return output.ToString();
        }

        public string Encrypt(string input)
        {
            var output = new StringBuilder();
            var a = key.Item1;
            var b = key.Item2;
            foreach (var x in input)            
                output.Append((char)((a * (x - 'A') + b) % 26 + 'A'));            
            return output.ToString();
        }

        public void PrintKey()
        {
            char a = (char)(key.Item1 + 'A');
            char b = (char)(key.Item2 + 'A');
            Console.WriteLine("Key - " + a + "" + b);
        }
        private void SetRamdomKey()
        {
            int[] allowedNum = new int[] { 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25 };
            var rnd = new Random();
            var a = allowedNum[rnd.Next() % allowedNum.Length];
            var b = rnd.Next() % 26;
            key = Tuple.Create(a, b);
        }
        private int A_INV(int a)
        {
            for (int x = 1; x < 26; x++)
                if (a * x % 26 == 1)
                    return x;
            throw new ArgumentException("I can't find");
        }
    }
}
