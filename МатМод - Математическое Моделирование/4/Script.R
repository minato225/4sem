f1 <- function(x) sqrt(1 + x)
f1Real <- function(x) exp(-x) * sqrt(1 + x)

f2 <- function(x, y) x ^ 2 + y ^ 2

MC1 <- function(n) sum(f1(rexp(n)))/n
MC2 <- function(n) 2 * sum(f2(runif(n), runif(n)))/n

a = 10
N = 1000
size = N/a
real_I1 = integrate(f1Real, 0, Inf)
scales <- seq(a, N, a)

#график
plot(sapply(scales, MC1), type = "l", col = "red")
lines(1:size, rep(real_I1$value,size), col = "green", lwd = 3)

print(real_I1$value)

real_I2 = 1.3376
scales <- seq(a, N, a)

#график
plot(sapply(scales, MC2), type = "l", col = "red")
lines(1:size, rep(real_I2, size), col = "green", lwd = 3)

print(real_I2)
