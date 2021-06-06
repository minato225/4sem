using System.Collections.Generic;

namespace Lab_11
{
    public static class Logic
    {
        public static List<string> Divider(this string str, int blockLength)
        {
            var blocks = new List<string>(str.Length / blockLength + 1);
            for (var i = 0; i < str.Length; i += blockLength)
            {
                if (str.Length - i > blockLength)
                    blocks.Add(str.Substring(i, blockLength));
                else
                    blocks.Add(str.Substring(i, str.Length - i) + 
                               new string(' ', blockLength - (str.Length - i)));
            }
            
            return blocks;
        }
    }
}