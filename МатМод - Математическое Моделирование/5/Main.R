markov_gen <- function(P, state) {
  rnd <- runif(1, 0, 1)
  for (i in 1:N) {
    rnd <- rnd - P[state, i]
    if (rnd <= 0)
      return(i)
  }
  N
}

markov_solve <- function(A, n, f, P, markov_len, markov_cnt) {
  X = c(1,1,1)
  for (coordinate in 1:n) {
    x <- 0
    for (i in 1:markov_cnt) {
      prev_m <- coordinate
      prev_q <- 1
      for (j in 2:markov_len) {
        next_m <- markov_gen(P, prev_m)
        next_q <- 0
        if (P[prev_m, next_m] > 0)
          next_q <- prev_q * A[prev_m, next_m] / P[prev_m, next_m]
        x <- x + next_q * f[next_m]
        prev_q <- next_q
        prev_m <- next_m
      }
    }
    X[coordinate] <- f[coordinate] + x / markov_cnt
  }
  
  X
}

N <- 3

A = matrix(c(1.2, -0.3, 0.4,
             0.4, 0.7, -0.2,
             0.2, -0.3, 0.9) , N, N, byrow = TRUE)

A
f = c(-4, 2, 0)
# Приводим к виду X = BX + f
B = matrix(c(-0.2, 0.3, -0.4,
             -0.4, 0.3, 0.2,
             -0.2, 0.3, 0.1), N, N, byrow = TRUE)

I <- diag(N)
# Матрица переходных вероятностей
P = matrix(rep(1 / 3, N ^ 2), N, N, byrow = TRUE)
# Задаем длину цепи Маркова
markov_len = 50
# Число моделирований цепей Маркова
markov_cnt = 1000

X = markov_solve(B, N, f, P, markov_len, markov_cnt)
print(X)

# найдем настоящее решение с помощью встроенной библиотеки
X1 <- solve(A, f)
X1
# найдем норму невязки
sqrt(sum((X - X1) ^ 2))

# Построить график зависимости точности решения 
# от длины цепи маркова и числа смоделированных 
# цепей маркова.

x<-scan('Cnt.txt')
y<-scan('Len.txt')
z<-scan('Norms.txt')
axx <- list(nticks = 4,  range = x)
axy <- list(nticks = 4,range = y)
axz <- list(nticks = 4,range = z)

fig <- plot_ly(x = ~x, y = ~y, z = ~z, type = 'mesh3d') 
fig <- fig %>% layout(scene = list(xaxis=axx,yaxis=axy,zaxis=axz))

fig
