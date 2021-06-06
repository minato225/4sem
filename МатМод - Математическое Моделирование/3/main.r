LCG = function(m, a, c) {
  rng = vector(length = N)
  
  d = a
  
  for (i in 1:N) {
    d = (a * d + c) %% m
    rng[i] = d / m
  }
  
  return(rng)
}
Normal = function(m, S, rnd){
  gen = vector(length = N)
  
  for(i in seq(1, N - 1, 2)){
    gen[i + 0] <- m + S*sqrt(-2*log(rnd[i])) * sin(2*rnd[i+1] * pi)
    gen[i + 1] <- m + S*sqrt(-2*log(rnd[i])) * cos(2*rnd[i+1] * pi)
  }
  
  return (gen)
}
W = function(a, b, rnd){
  gen = vector(length = N)
  for (i in 1:N)
    gen[i] = (-log(rnd[i])/b)^(1/a)
  
  return(gen)
}
LG = function(a, b, rnd){
  gen = vector(length = N)
  for (i in 1:N)
    gen[i] = a + b*log(rnd[i]/(1-rnd[i]))
  
  return(gen)
}
Chi_Square = function(m, rnd){
  gen = vector(length = N)
  for (i in 1:N)
    for(j in 1:m)
      gen[i] = gen[i] + rnd[sample(1:N, 1)]^2
  
  return(sqrt(gen))
}
Fisher = function(m,l, rnd){
  Chi_Square(m, rnd)*l/(Chi_Square(l,rnd)*m)
}

#------------------------------------------------------------------------------#
Kol_Test = function(dist, pDist, a, b){
  supremum = 0
  less_count = 0
  for (i in 1:N){
    less_count = 0
    for (j in 1:N)
      if (dist[j] <= dist[i])
        less_count = less_count +  1 
    
    Fn = less_count / N
    supremum = max(abs(Fn - pDist(dist[i], a, b)),supremum)
  }
  
  return (sqrt(N) * supremum < 1.36)
}
Chi_Test = function(dist, pDist, a, b){
  freedom_degrees = 30
  critical_point = 43.77
  min_value = min(dist)
  max_value = max(dist)
  delta = (max_value - min_value) / freedom_degrees
  divisions = as.vector(rep(0, freedom_degrees))
  
  for (value in dist){
    index = as.numeric(floor((value - min_value) / delta)%%freedom_degrees)
    if (index == freedom_degrees){
      divisions[freedom_degrees] = divisions[freedom_degrees] + 1
    }
    else{
      divisions[index] = divisions[index] +  1
    }
  }
  
  hi = 0.0
  temp = 0.0
  for (i in 1:freedom_degrees){
    temp = (N * (pDist(min_value + (i + 1) * delta,a,b) - pDist(min_value + i * delta,a,b)))
    hi = hi + (divisions[i] - temp)^2 / temp
  }
  
  return (hi < critical_point)
}

#------------------------------------------------------------------------------#
RealN   = function(m, S, x){
  0.5 + Fi((x - m)/S)
}
RealW   = function(a, b, x){
  1 - exp(-(x/a)^b)
}
RealLG  = function(a, b, x){
  1/(1+exp((a - x)/b))
}
RealChi = function(x, a, b){
  0.5 + Fi(sqrt(2*x) - sqrt(2*N - 1))
}
RealF   = function(a, b, x){
  pf(x, a, b)
}

#------------------------------------------------------------------------------#
#факториал
Fi = function(x){
  1-0.5*(1+0.196854*x + 0.115194*x^2 + 0.000344*x^3 + 0.019527*x^4)^(-4)
}
#математичекское ожидание
E = function(dist){
  sum(dist)/N
}
#дисперси€
D = function(dist){
  e = E(dist)
  sum((dist - e)^2) / N
}
#вывод графика
scatter_plot = function(gen1, gen2, plotter_name){
  ggplot(data = data.frame(gen1, gen2), aes(x = gen1, y = gen2, color = gen2)) +
    geom_point(shape = 19) + 
    scale_color_gradient(low="blue", high="red") +
    ggtitle(plotter_name) + 
    theme_classic()
}

#------------------------------------------------------------------------------#
N = 1000
randomGen = LCG(2551*18*2**12, 2551*2*3*2 + 1, 17)

#------------------------------------------------------------------------------#
name = "Ќормальное распределение"
Normal_Gen = Normal(-2, 1, randomGen)
scatter_plot(Normal_Gen[-N], Normal_Gen[-1], name)
sprintf("real E %f teor E %f", E(Normal_Gen), -2)
sprintf("real D %f teor D %f", D(Normal_Gen), 1)

#------------------------------------------------------------------------------#
name = "распределение ¬ейбула"
W_Gen = W(0.5, 1, randomGen)
scatter_plot(W_Gen[-N], W_Gen[-1], name)
sprintf("real E %f teor E %f", E(W_Gen), 2)
sprintf("real D %f teor D %f", D(W_Gen), 20)

#------------------------------------------------------------------------------#
name = "логистическое распределение"
LG_Gen = LG(-1, 2, randomGen)
scatter_plot(LG_Gen[-N], LG_Gen[-1], name)
sprintf("real E %f teor E %f", E(LG_Gen), -1)
sprintf("real D %f teor D %f", D(LG_Gen), ((2*pi)^2)/3)

#------------------------------------------------------------------------------#
name = "хи-квадрат распределение"
Chi_Gen = Chi_Square(4, Normal(0,1, randomGen))
scatter_plot(Chi_Gen[-N], Chi_Gen[-1], name)
sprintf("real E %f teor E %f", E(Chi_Gen), 4)
sprintf("real D %f teor D %f", D(Chi_Gen), 8)

#------------------------------------------------------------------------------#
name = "распределение фишера"
Fisher_Gen = Fisher(3,5, Normal(0,1, randomGen))
scatter_plot(Fisher_Gen[-N], Fisher_Gen[-1], name)
sprintf("real E %f teor E %f", E(Fisher_Gen), 1.667)
sprintf("real D %f teor D %f", D(Fisher_Gen), 11.111)
