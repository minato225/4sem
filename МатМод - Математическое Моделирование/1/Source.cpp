#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>

using namespace std;

const int K = 256;
int b = 262147;
int d = 102299;
int a0 = 262147;
const int N = 1000;
unsigned int M = 2147483648;
const int k = 100;

vector<double> B(N), C(N);
vector<double> V(K), A(N);

void MCG_rand() {
	int A_i[N];
	A_i[0] = a0;
	B[0] = (double)a0 / M;

	for (int i = 1; i < N; i++) {
		A_i[i] = A_i[i - 1] * b % M;
		B[i] = (double)A_i[i] / M;
	}
}

void SCG_rand() {
	int A_i[N];
	A_i[0] = a0;
	C[0] = (double)a0 / M;

	for (int i = 1; i < N; i++) {
		int a = A_i[i - 1];
		A_i[i] =  (d*a*a+ a * b) % M;
		C[i] = (double)A_i[i] / M;
	}
}

void MacLaren_Marsaglia() {
	copy(B.begin(), B.begin() + K, V.begin());

	for (int i = 0; i < N; i++) {
		int s = (int)abs(C[i] * K);	
		A[i] = V[s];
		V[s] = C[(i + K) % N];
	}	
}

double calcChiSquare(vector<double> arr) {
	double HI = 0;
	int E = 10; // 1000*(1/100)ожидание
	vector<int>dataCounts(k);

	for (auto x : arr) 
		dataCounts[(int)(x * k)]++;

	for (int i = 0; i < k; i++)
		HI += pow(dataCounts[i] - E, 2);
	
	return HI/N; // e = 0.05 k = 100
}

double Kolmogorov_test(vector<double> arr) {
	double D_n = 0;
	double E = 0.01; // 1/100
	int hits = 0;
	for (double x = 0; x <= 1; x += E) {
		for (int j = 0; j < N; j++)
			if (arr[j] < x)
				hits++;
		cout << hits << " ";
		double F_ksi = ((double)hits) / N;
		double F = x;
		hits = 0;
		D_n = max(D_n ,abs(F_ksi-F));
	}
	return D_n*sqrt(N); // для е = 0.005 к = 100
}

int main() {
	MCG_rand();
	SCG_rand();
	MacLaren_Marsaglia();
	cout << calcChiSquare(A) << " " << Kolmogorov_test(A);
	// string pirson_says = calcChiSquare(A) ? "YES" : "NO";
	// string kolmogorov_says = Kolmogorov_test(A) ? "YES" : "NO";
	//cout << pirson_says << endl << kolmogorov_says;
}