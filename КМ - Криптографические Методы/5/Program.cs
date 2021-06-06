using System;
using System.Collections.Generic;
using System.IO;
using System.Numerics;
using System.Security.Cryptography;
using System.Text;

namespace Hash
{
    public static class Program
    {
        private const string FileNameText = "text.txt";
        private const int e = 0b10000000000000001; // 65537 то число имеет мало единичных битов.
        private const int BitsLengthRsa = 1<<10; // 1024 бит

        public static void Main() {
            Console.WriteLine($"text: {File.ReadAllText(FileNameText)}\n");
            // Исходный текст.
            var text = File.ReadAllBytes(FileNameText);
            
            // хэш.
            var hash = Hash(text);
            Console.WriteLine($"hash: {hash}\n");
            
            // p и q взаимно простые по 512 бита 64 байт.
            var (p, q) = (GetBigPrime(BitsLengthRsa), GetBigPrime(BitsLengthRsa));
            Console.WriteLine($"p: {p}");
                //Console.WriteLine(p.ToBinaryString());//WriteByte(p);
            Console.WriteLine($"q: {q}");
                //Console.WriteLine(q.ToBinaryString()); //WriteByte(q);

            // n.
            var n = q * p;
            Console.WriteLine($"n: {n}\n");
            
            // функция эйлера для нахождения количества натуральных чисел, не превосходящих n и взаимно простых с n.
            // в данном случае Φ(n) = (p - 1) * (q - 1), где n = q*p.
            var φ = Φ(p, q);
            Console.WriteLine($"Ф(n): {φ}\n");
            
            // секретный ключ с условие d*e≡1(mod φ(n)).
            // нахождение через расширенный алгоритм Евклида.
            var d = ExtendedGcd(e, φ).x.Mod(φ);
            Console.WriteLine($"d: {d}\n");
            
            //Electronic Digital Signature. Боб вычисляет по открытому колючу значение eds.
            // eds ≡ m^e(mod n)
            var eds = ModPower(hash, d, n);
            eds ^= 1;
            Console.WriteLine($"eds: {eds}\n");

            // По полученному eds. Расшифровываем сообщения Боба. 
            // m ≡ eds^d(mod n)
            var message = ModPower(eds, e, n);
            Console.WriteLine($"message: {message}\n");
            
            // Проверяем равенство подписей
            Console.WriteLine(message == hash ? "Is Alice!" : "Is eve :(" + "\n");
        }

        #region RSA

        private static BigInteger Mod(this BigInteger x, BigInteger m) => (x % m + m) % m;
        private static (BigInteger g,BigInteger x,BigInteger y) ExtendedGcd(BigInteger a, BigInteger b)
        {
            if (a == 0) 
                return (b, 0, 1);
            var (g, x, y) = ExtendedGcd(b % a, a);
            return (g, y - (b / a) * x, x);
        }
        private static BigInteger ModPower(BigInteger value, BigInteger pow, BigInteger n)
        {
            value %= n;
            BigInteger result = 1;
            while (pow > 0)
            {
                if (pow % 2 == 1)
                    result = result * value % n;
                value = value * value % n;
                pow /= 2;
            }

            return result;
        }
        private static BigInteger Φ(BigInteger p, BigInteger q) => (p - 1) * (q - 1);
        private static BigInteger GetBigPrime(int n)
        {
            var rng = new RNGCryptoServiceProvider();
            var bytes = new byte[n / 8];
            do rng.GetBytes(bytes);
            while (!new BigInteger(bytes).IsPrime());
            return new BigInteger(bytes);
        }
        #endregion
        
        #region Hash
        private static BigInteger Hash(byte[] text)
        {
            var b = ByteHash(text);
            return new BigInteger(Math.Abs(b[0] | (b[1] << 8) | (b[1] << 16) | (b[1] << 24)));
        }
        private static byte[] ByteHash(byte[] txt)
        {
            var text = new List<byte>(txt);
            var length = text.Count;

            for (var i = 0; i < text.Count % 4; i++)
                text.Add(0);

            text.Add((byte) (length >> 24));
            text.Add((byte) (length >> 16));
            text.Add((byte) (length >> 8));
            text.Add((byte) length);

            var y = new byte[] {100, 134, 78, 9};
            for (var i = 0; i < text.Count; i += 4)
            {
                var x = new[] {text[i], text[i + 1], text[i + 2], text[i + 3]};
                y = Σ(x, y);
            }

            return y;
        }
        private static byte[] Σ(byte[] x, byte[] y) {
            var xy = Xor(x, y);
            return Xor(F(xy, x), xy);
        }
        private static byte[] Xor(byte[] a, byte[] b) {
            var output = new byte[a.Length];
            for (var i = 0; i < a.Length; ++i)
                output[i] = (byte) (a[i] ^ b[i]);
            return output;
        }

        #region Block Cipher
        private static byte[] F(byte[] input, byte[] key) {
            var output = new byte[input.Length];
            input.CopyTo(output, 0);
            for (var j = 0; j < 8; j++)
                for (var i = 0; i < input.Length; i += 2) {
                    var (x1, x2) = (output[i], output[i + 1]);
                    var tmp = x2 ^ key[j % 4];
                    var (s1, s2) = (SBox1(tmp >> 4 & 0xF) << 4, SBox2(tmp & 0xF));
                    tmp = s1 | s2;
                    tmp = CycleShiftLeft(tmp, 3);
                    tmp ^= x1;
                    (output[i], output[i + 1]) = (x2, (byte) tmp);
                }

            return output;
        }
        private static int CycleShiftLeft(int a, int s) => ((a << s) & 0xFF) | (a >> 8 - s);
        private static int SBox1(int x) => (int) (Math.Pow(3, x) % 17 + 2) % 16;
        private static int SBox2(int x) => (int) (Math.Pow(5, x) % 17 + 7) % 16;
        
        #endregion

        #endregion

        #region Additional Functions
        private static string ToBinaryString(this BigInteger a) {
            var sb = new StringBuilder();
            do sb.Insert(0, a % 2);
            while ((a /= 2) > 0);
            return sb.ToString();
        }
        #endregion
    }
}