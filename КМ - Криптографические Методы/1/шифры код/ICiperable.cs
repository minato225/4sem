using System;
using System.Collections.Generic;
using System.Text;

namespace Lab_1
{
    public interface ICiperable
    {
        string Encrypt(string input);
        string Decrypt(string input, string key);
        void PrintKey();
    }
}
