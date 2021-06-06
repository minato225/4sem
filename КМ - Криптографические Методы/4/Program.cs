using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace Hash
{
    public static class Program
    {
        private const string FileNameText = "text.txt";

        public static void Main() {
            //var text = new List<byte>(File.ReadAllBytes(FileNameText));
            //WriteFile(Hash(text));

            WriteConsole(Hash(new List<byte> {12, 196, 207, 87}));
            WriteConsole(Hash(new List<byte> {1, 100, 132, 7}));

            //var (x1, x2) = BirthAttack();
            //WriteConsole(x1);
            //WriteConsole(x2);
        }

        private static byte[] Hash(List<byte> text)
        {
            var length = text.Count;
            
            for (var i = 0; i < text.Count % 4; i++)
                text.Add(0);

            text.Add((byte)(length >> 24));
            text.Add((byte)(length >> 16));
            text.Add((byte)(length >> 8));
            text.Add((byte)length);
            
            var y = new byte[]{100, 134, 78, 9};
            for (var i = 0; i < text.Count; i += 4) {
                var x = new [] {text[i], text[i + 1], text[i + 2], text[i + 3]};
                y = σ(x, y);
            }

            return y;
        }
        private static byte[] σ(byte[] x, byte[] y) {
            var xy = Xor(x, y);
            return Xor(F(xy, x), xy);
        }

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
        private static byte[] Xor(byte[] a, byte[] b) {
            var output = new byte[a.Length];
            for (var i = 0; i < a.Length; ++i)
                output[i] = (byte) (a[i] ^ b[i]);
            return output;
        }
        private static void WriteFile(byte[] hash) {
            using var writer = new StreamWriter("OutHash.txt", true);
            foreach (var b in hash) 
                writer.Write(BitFormat(b));
            writer.Write("\n");
        }

        private static void WriteConsole(byte[] hash)
        {
            #region
            hash = new byte[] {12, 196, 207, 87};
                #endregion
            foreach (var b in hash) 
                Console.Write(BitFormat(b));
            Console.Write("\n");
        }
        private static string BitFormat(byte b) => $"{Convert.ToString(b, 2).PadLeft(8, '0')} ";

        private static (byte[] x1, byte[] x2) BirthAttack()
        {
            var rnd = new Random();
            var h = new byte[1 << 20][];
            byte[] x;
            int y;
            
            while (true) {
                x = Int2ByteArray(rnd.Next());
                y = ArrayByte2Int(Hash(x.ToList())) % (1 << 20);
                
                if (h[y] != null && !x.SequenceEqual(h[y])) 
                    break;
                h[y] = x;
            }

            return (h[y], x);
        }

        private static int ArrayByte2Int(byte[] b) 
            => Math.Abs(b[0] | (b[1] << 8) | (b[1] << 16) | (b[1] << 24));

        private static byte[] Int2ByteArray(int i) 
            => new[] {(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }
}