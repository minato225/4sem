using System.Numerics;
using System.Security.Cryptography;

namespace Hash
{
    public static class PrimeExtensions 
    {
        public static bool IsPrime(this BigInteger source, int certainty = 1) 
        { 
            return source switch
            {
                _ when source == 2 || source == 3 => true,
                _ when source < 2 || source % 2 == 0 => false,
                _ => IsProbablyPrime(source, certainty),
            };

            static bool IsProbablyPrime(BigInteger source, int certainty)
            {
                var d = source - 1;
                var s = 0;

                while (d % 2 == 0) {
                    d /= 2;
                    s++;
                }

                var rng = RandomNumberGenerator.Create();
                var bytes = new byte[source.ToByteArray().LongLength];

                for (var i = 0; i < certainty; i++) {
                    BigInteger a;
                    do {
                        rng.GetBytes(bytes);
                        a = new BigInteger(bytes);
                    } while (a < 2 || a >= source - 2);

                    var x = BigInteger.ModPow(a, d, source);
                    if (x == 1 || x == source - 1) continue;

                    for (var r = 1; r < s; r++) {
                        x = BigInteger.ModPow(x, 2, source);
                        if (x == 1) return false;
                        if (x == source - 1) break;
                    }

                    if (x != source - 1) return false;
                }

                return true;
            }
        }
    }
}