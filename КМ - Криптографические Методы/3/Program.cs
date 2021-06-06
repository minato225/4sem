using System;
using System.IO;
using System.Linq;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Lab_3_KM
{
    public static class Program
    {
        private const int M = 3;
        private static byte[] _text;
        private static byte[] _keys = new byte[8];
        private const string FileNameKey = "key.txt";
        
        private const string FileNameText = "apple.png";
        private const string FileNameCipher = "1apple.png";
        
        private const string FileNameDText = "1apple.png";
        private const string FileNameDCipher = "2apple.png";
        
        public static void Main()
        {
            Console.WriteLine("Choose option");
            Console.WriteLine("1 - Encrypt\n2 - Decrypt");
            var choose = Console.ReadLine() == "1";
            _text = File.ReadAllBytes(choose ? FileNameText : FileNameDText);
            var f = _text.Length % 2 != 0;
            if (_text != null && f)
            {
                Array.Resize(ref _text, _text.Length + 1);
                _text[^1] = 0;
            }

            var theta = File.ReadAllBytes(FileNameKey);
            for (var i = 0; i < 8; i++)
                _keys[i] = theta[i % 4];

            if (choose)
            {
                for (var i = 0; i < 8; i++)
                {
                    EncryptOneRound(i);
                    Console.WriteLine(i);
                }
            }
            else
            {
                for (var i = 7; i >= 0; i--)
                {
                    DecryptOneRound(i);
                    Console.WriteLine(i);
                }
            }

            File.WriteAllBytes(choose ? FileNameCipher : FileNameDCipher, _text);
            if (f) _text[^1] = 0;
        }
		
        private static void EncryptOneRound(int keyNumber)
        {
            for (var i = 0; i < _text.Length; i++)
            {
                var (x1, x2) = (_text[i++], _text[i]);
                var tmp = x2 ^ _keys[keyNumber];
                var (s1, s2) = (SBox1(tmp >> 4 & 0xF) << 4, SBox2(tmp & 0xF));
                tmp = s1 | s2;
                tmp = CycleShiftLeft(tmp, 3);
                tmp ^= x1;
                (_text[i - 1], _text[i]) = (x2, (byte) tmp);
            }
        }

        private static void DecryptOneRound(int keyNumber)
        {
            for (var i = 0; i < _text.Length; i++)
            {
                var (x1, x2) = (_text[i++], _text[i]);
                var tmp = x1 ^ _keys[keyNumber];
                var (s1, s2) = (SBox1(tmp >> 4 & 0xF) << 4, SBox2(tmp & 0xF));
                tmp = s1 | s2;
                tmp = CycleShiftLeft(tmp, 3);
                tmp ^= x2;
                (_text[i - 1], _text[i]) = ((byte) tmp, x1);
            }
        }

        private static int CycleShiftLeft(int a, int s) => ((a << s) & 0xFF) | (a >> 8 - s);
        private static int SBox1(int x) => (int) (Math.Pow(3, x) % 17 + 2) % 16;
        private static int SBox2(int x) => (int) (Math.Pow(5, x) % 17 + 7) % 16;
    }
}