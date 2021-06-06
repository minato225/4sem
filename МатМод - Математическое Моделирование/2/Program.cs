using System;
using System.Collections.Generic;
using System.Linq;

namespace Lab_2
{
    public class Program
    {
        private static Random random = new Random();
        private const int N = 5000;
        private static long b = 262147;
        private static long a0 = 262147;
        private static int M = int.MaxValue;
        private static readonly List<double> randList = new List<double>();
        public static void Main(string[] args)
        {
            // Инициализация случайном последовательности чисел [0;1]
            MCG_rand();
            var Bi_E = E(0.2, Bi);
            var NBi_E = E(0.8, Bi_Negative);
            var П_E = E(0.3, П);
            var G_E = E(0.3, G);

            Console.WriteLine($" Bi(7,0.2) практическое Е - {Bi_E} теория - 1.4"); // np
            Console.WriteLine($"-Bi(4,0.8) практическое Е - {NBi_E} теория - 1"); // nq/p
            Console.WriteLine($"П(0.3) практическое Е - {П_E} теория - 0.3"); // λ
            Console.WriteLine($"G(0.3) практическое Е - {G_E} теория - 3.233334"); // 1 + q/p
            Console.WriteLine();
            Console.WriteLine($" Bi(7,0.2) практическое D - {D(0.2, Bi_E ,Bi)} теория - 1.12"); // npq
            Console.WriteLine($"-Bi(4,0.8) практическое D - {D(0.8, NBi_E ,Bi_Negative)} теория - 1.25"); // nq/p^2
            Console.WriteLine($"П(0.3) практическое D - {D(0.3, П_E,П)} теория - 0.3"); // λ
            Console.WriteLine($"G(0.3) практическое D - {D(0.3, G_E, G)} теория - 7.7778"); // q/p^2


            Console.WriteLine("практические Частоты геометрического");
            Console.WriteLine($"хи квадрат Геометрическое - {Pirson(G, 0.3, out int free_deg, Real_F_G)}");
            Console.WriteLine("степень свободы " + free_deg);

            Console.WriteLine("теоретические Частоты геометрического");
            for (int i = 0; i < free_deg; i++)
                Console.WriteLine("{0:f4}",N*Real_F_G(0.3, i));


            Console.WriteLine("практические Частоты Пуассона");
            Console.WriteLine($"хи квадрат Пуассона - {Pirson(П, 0.3, out free_deg, Real_F_П)}");

            Console.WriteLine("теоретические Частоты Пуассона");
            for (int i = 0; i < free_deg; i++)
                Console.WriteLine("{0:f4}",N*Real_F_П(0.3, i));

            Console.WriteLine("степень свободы " + free_deg);
        }

        private static IEnumerable<int> П(double λ)
        {
            double p = Math.Exp(-λ);
            for (int i = 0; i < N; i++) 
            {
                var k = 0;
                var r = randList[i];
                while (r > p)
                {
                    r *= randList[random.Next(N)];
                    ++k;
                }

                yield return k;
            }
        }

        private static IEnumerable<int> Bi_Negative(double p)
        {
            for (int i = 0; i < N; i++)
                yield return randList.GetRange(random.Next(N - 4), 4).Sum(a => (int)Math.Floor(Math.Log(a) / Math.Log(1 - p)));
        }

        private static IEnumerable<int> G(double p)
        {
            for (int i = 0; i < N; i++)
                yield return (int)Math.Ceiling(Math.Log(randList[i]) / Math.Log(1 - p));
        }

        private static IEnumerable<int> Bi(double p)
        {
            for (int i = 0; i < N; i++)
                yield return randList.GetRange(random.Next(N - 7), 7).Count(a => p > a);
        }

        private static double E(double p, Func<double, IEnumerable<int>> distribution)
        {
            return distribution(p).Average();
        }

        private static double D(double p, double E, Func<double, IEnumerable<int>> distribution)
        {
            return distribution(p).Average(x => Math.Pow(x - E, 2));
        }

        private static double Pirson(Func<double, IEnumerable<int>> distribution, double p, out int free_deg, Func<double,int, double> Real_F)
        {
            var dataCounts = new List<int>();
            var HI = 0.0;
            var sortedlist = distribution(p).ToList();
            sortedlist.Sort();
            var set = sortedlist.ToHashSet();

            foreach (var x in set)
                dataCounts.Add(sortedlist.Count(y => y == x));

            free_deg = dataCounts.Count;

            dataCounts.ForEach(x => Console.WriteLine(x));

            var i = 0;
            foreach (var x in dataCounts)
            {
                var f = Real_F(p, i++);
                HI += Math.Pow(x - f * N, 2) / (f * N);
            }

           return HI;
        }

        private static double Real_F_П(double λ, int k)
        {
            return Math.Exp(-λ) * Math.Pow(λ, k)/Fact(k);
        }

        private static double Real_F_G(double p, int i)
        {            
            return Math.Pow(1 - p, i) * p;
        }

        private static int Fact(int n)
        {
            if (n == 0)
                return 1;
            return Fact(n - 1) * n;
        }
        private static void MCG_rand()
        {
            var A_i = new List<long>(N) { a0 };
            randList.Add((double)a0 / M);

            for (int i = 1; i < N; i++)
            {
                A_i.Add(A_i[i - 1] * b % M);
                randList.Add((double)A_i[i] / M);
            }
        }
    }
}
