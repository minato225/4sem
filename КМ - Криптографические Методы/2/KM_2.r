getbit = function(val, n){
  bitwAnd(bitwShiftR(val, n), 1) != 0
}

setbit = function(val, n){ 
  bitwOr(val,bitwShiftL (1, n))
}

MCG = function(m, a, c) {
  rng = vector(length = N)
 
  d = a
  
  for (i in 1:N) {
    d = (a * d + c) %% m
    rng[i] = d / m
  }
  
  return(rng)
}

Marsaglia_Gen = function(gen1,gen2,K){
  V = gen1[1:K]
  a = numeric(N)
  for (i in 1:N){
    s = floor(gen2[i]*K)+1 
    a[i] = V[s]
    V[s] = gen1[i]
  }
  
  return(a)
}

LFSR_Next = function(start, i1, i2, len_bits){
  
  result = bitwShiftR(start, 1)
  
  pos_1 = len_bits - i1
  pos_2 = len_bits - i2
  
  first = getbit(start, pos_1)
  second = getbit(start, pos_2)
  
  new = bitwXor(as.numeric(first), as.numeric(second))
  
  if (isTRUE(new == 1))
    result = setbit(result, len_bits - 1)
  
  return (result)
}

LFSR_Gen = function(start, it1,it2, len_bits, B){
  rng = vector(length = N)
  
  for (i in 1:N){
    num = 0
    for(j in 1:B){
      start = LFSR_Next(start, it1,it2, len_bits)
      last = bitwShiftL(as.numeric(getbit(start, len_bits - 1)), j)
      num = num + last
      rng[i] = num
    }
  }
  
  return(rng)
}

scatter_plot = function(gen1, gen2, plotter_name){
  ggplot(data = data.frame(gen1, gen2), aes(x = gen1, y = gen2, color = gen2)) +
    geom_point(shape = 19) + 
    scale_color_gradient(low="blue", high="red") +
    ggtitle(plotter_name) + 
    theme_classic()
}

#------------------------------------------------------------------------------#
N = 1000

mcg1 = MCG(103,17,2)
mcg2 = MCG(2551*18*2**12, 2551*2*3*2, 17)
mcg3 = MCG(2551*18*2**12, 2551*2*3*2+1, 17)

#Marsaglia1 = Marsaglia_Gen(mcg1, mcg2, K = 64)
#Marsaglia2 = Marsaglia_Gen(mcg2, mcg1, K = 256)

#lfsr1 = LFSR_Gen(start = 15, 5, 3, len_bits = 5, 8)
#lfsr2 = LFSR_Gen(start = 2047, 11, 9, len_bits = 11, 13)

#------------------------------------------------------------------------------#

scatter_plot(mcg1[-1], mcg1[-N], 'LCG 1')
scatter_plot(mcg2[-1],  mcg2[-N], 'LCG 2')
scatter_plot(mcg3[-1],  mcg3[-N], 'LCG 3')

#scatter_plot(Marsaglia1[-1], Marsaglia1[-N], 'макларена—марсальи 1')
#scatter_plot(Marsaglia2[-1], Marsaglia2[-N], 'макларена—марсальи 2')

#scatter_plot(lfsr1[-1], lfsr1[-N], 'LFSR 1')
#scatter_plot(lfsr2[-1], lfsr2[-N], 'LFSR 2')

saveGIF({
  for (i in 1:20){
    lfsr = LFSR_Gen(1, 10, 3, 10, i)
    x <- lfsr[-N]
    y <- lfsr[-1]
    scatter2D(x, y, colvar = x, col = "blue", pch=20, cex=0.5, theta=50, main = paste('разбиение на ', i))
  }
}, movie.name = 'lcg_poor.gif')