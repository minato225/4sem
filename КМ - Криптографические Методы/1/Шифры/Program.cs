using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace Lab_1_4_5_
{
    class Program
    {
        static void Main(string[] args)
        {
            var input = new StreamReader(@"C:\Users\Рома\Desktop\ct09.txt");
            #region
            /*
            var input1 = new StreamReader(@"C:\Users\Рома\Desktop\alice_in_wonderland.txt");
            var input2 = new StreamReader(@"C:\Users\Рома\Desktop\glass.txt");

            var outputShortWonderland = new StreamWriter(@"C:\Users\Рома\Desktop\WonderLand-Short.txt");
            var outputShortGlass = new StreamWriter(@"C:\Users\Рома\Desktop\Glass-Short.txt");

            var wonderlandText = input1.ReadToEnd();
            var glassText = input2.ReadToEnd();

            var shortWonderland = Regex.Replace(wonderlandText, "\\s+|[-.?!)(,:*’”“‘—'`;\"]", "");
            var shortGlass = Regex.Replace(glassText, "\\s+|[-.?!)(,:*’”“‘—'`;\"]", "");

            outputShortWonderland.Write(shortWonderland);
            outputShortGlass.Write(shortGlass);
            */
            #endregion
            #region

            var mess = input.ReadLine();
            var infoWorld = new Dictionary<char, char>
             {
                 {'U','e'},//e e +
{'M','.'},//t t +
{'J','.'},//o a a +-
{'Q','.'},//a o o +-
{'V','.'},//i i +-
{'B','.'},//n h +-
{'D','.'},//s n +-
{'K','.'},//h r s +-
{'I','.'},//r h r +-
{'Y','.'},//l d d +-
{'N','.'},//u l d l +-
{'A','.'},//d c u +-
{'W','.'},//y u w +-
{'C','.'},//m g +-
{'X','.'},//w f c +-
{'L','.'},//g f p y —
{'S','.'},//f g m +-
{'R','.'},//c y w f +-
{'E','.'},//b p y p —
{'Z','.'},//p b b —
{'G','.'},//k v k +-
{'T','.'},//v k v +
{'O','.'},//j x q +-
{'H','.'},//x j x +-
{'P','.'},//z q j +
{'F','.'}//q z z +
             };

            var data = new List<Tuple<double, char>>();
            for (int i = 'A'; i <= 'Z'; i++)
            {
                var x = mess.Count(x => x == (char)i);
                var d = 1.0 * x / mess.Length;
                data.Add(Tuple.Create(d, (char)i));
            }

            data.Sort();
            data.Reverse();
            int j = 0;
            var newAl = string.Empty;
            foreach (var c in data)
            {
                newAl += c.Item2;
                Console.WriteLine("{0:f4} -  {1} - {2}", c.Item1, c.Item2, infoWorld[c.Item2]);
                j++;
            }

            var v = new StringBuilder();
            foreach (var x in mess)
            {
                v.Append(infoWorld[x]);
            }

            Console.WriteLine(v);

            #endregion
        }
    }
}
