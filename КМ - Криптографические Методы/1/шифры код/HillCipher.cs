using System;
using System.Text;

namespace Lab_1
{
    class HillCipher
    {
        public int[,] key = new int [3,3];
        public bool ifZero;
        public HillCipher()
        {
            SetRamdomKey();
           // key = new int[3,3]{ { 16, 22, 4 },{ 0, 18, 3 },{ 25, 23, 2 } };
            
        }
        
        #region DECRIPTION
        public string Decrypt(string input)
        {
            var ans = new StringBuilder();
            var size = (input.Length % 3 == 0) ? input.Length : ((input.Length / 3) + 1) * 3;
            var matrixIn = GetModifyIn(input, size);
           // this.key = GetModifyIn(key, 9);
            var det = GetDet(this.key);
            ifZero = det == 0;
            var X = EulerEx(det, 101);
            var inverseMatrixIn = inverseMatrix(X);
            var ansMatrix = MultyplyMatrix(matrixIn, inverseMatrixIn);

            for (int i = 0; i < size/3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    ans.Append((char)(ansMatrix[i, j] + 'A'));
                }
            }

            return ans.ToString();
        }

        private int EulerEx(int det, int length)
        {
            int x, y;
            int g = gcdex(det, length, out x, out y);
            x = (x % length + length) % length;
            if (det > 0 && x < 0) x += 37;
            else if (det > 0 && x < 0) x *= -1;
            return x;
        }

        private int gcdex(int a, int b, out int x, out int y)
        {
            if (a == 0)
            {
                x = 0; y = 1;
                return b;
            }

            int x1, y1;
            int d = gcdex(b % a, a, out x1, out y1);
            x = y1 - (b / a) * x1;
            y = x1;

            return d;
        }

        private int[,] inverseMatrix(int X)
        {
            var adj = new int[3,3];

            adj[0, 0] = (X * ((+key[1, 1] * key[2, 2] - key[2, 1] * key[1, 2]) % 101)) % 101;
            adj[1, 0] = (X * ((-key[1, 0] * key[2, 2] + key[2, 0] * key[1, 2]) % 101)) % 101;
            adj[2, 0] = (X * ((+key[1, 0] * key[2, 1] - key[2, 0] * key[1, 1]) % 101)) % 101;

            adj[0, 1] = (X * ((-key[0, 1] * key[2, 2] + key[2, 1] * key[0, 2]) % 101)) % 101;
            adj[1, 1] = (X * ((+key[0, 0] * key[2, 2] - key[2, 0] * key[0, 2]) % 101)) % 101;
            adj[2, 1] = (X * ((-key[0, 0] * key[2, 1] + key[2, 0] * key[0, 1]) % 101)) % 101;

            adj[0, 2] = (X * ((+key[0, 1] * key[1, 2] - key[1, 1] * key[0, 2]) % 101)) % 101;
            adj[1, 2] = (X * ((-key[0, 0] * key[1, 2] + key[1, 0] * key[0, 2]) % 101)) % 101;
            adj[2, 2] = (X * ((+key[0, 0] * key[1, 1] - key[1, 0] * key[0, 1]) % 101)) % 101;

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (adj[i, j] < 0)           
                        adj[i, j] += 101;

            return adj;
        }

        private int GetDet(int[,] arr)
        {
            return arr[0, 0] * arr[1, 1] * arr[2, 2] + arr[0, 1] * arr[1, 2] * arr[2, 0] + arr[1, 0] * arr[2, 1] * arr[0, 2] -
                   arr[2, 0] * arr[1, 1] * arr[0, 2] - arr[2, 1] * arr[1, 2] * arr[0, 0] - arr[1, 0] * arr[0, 1] * arr[2, 2];
        }
        #endregion

        #region ENCREPTION
        public string Encrypt(string input)
        {
            var size = (input.Length % 3 == 0) ? input.Length : ((input.Length / 3) + 1) * 3;

            var output = new StringBuilder();

            var matrixIn = GetModifyIn(input, size);

            var matrixOut = MultyplyMatrix(matrixIn, key);

            for (int i = 0; i < size/3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    output.Append((char)(matrixOut[i, j] + 'A'));
                }
            }

            return output.ToString();
        }
        private void SetRamdomKey()
        {
            var rnd = new Random();
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    key[i, j] = rnd.Next(0, 101);
                }
            }
        }
        #endregion

        private string GetModifyKey()
        {
            var strKey = new StringBuilder();
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    strKey.Append((char)(key[i, j] + 'A'));
                }
            }

            return strKey.ToString();
        }
        private int[,] MultyplyMatrix(int[,] a, int[,] b)
        {
            var row = a.GetUpperBound(0) + 1;
            var tmp = new int[row, 3];

            for (var i = 0; i < row; i++)            
                for (var j = 0; j < 3; j++)
                {
                    tmp[i, j] = 0;
                    for (var k = 0; k < 3; k++)                    
                        tmp[i, j] += a[i, k] * b[k, j];                    
                    tmp[i, j] %= 101;
                }

            return tmp;
        }
        private int[,] GetModifyIn(string input, int size)
        {
            var modIn = new int[size / 3, 3];
            for (int i = 0; i < size; i++)
            {
                modIn[i / 3, i % 3] = 101;
            }

            for (int i = 0; i < input.Length; i++)
            {
                modIn[i / 3, i % 3] = input[i] - 'A';
            }

            return modIn;
        }
        public string PrintKey()
        {
            return GetModifyKey();
        }
    }
}
