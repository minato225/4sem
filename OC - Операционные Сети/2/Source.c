#include <Windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <conio.h>

#pragma warning(disable : 4996)
const int TIME_SLEEP_MinMax = 7;
const int TIME_SLEEP_Avarage = 12;
volatile float Max = INT_MIN, Min = INT_MAX, Avarage = 0;
volatile int N;

DWORD WINAPI min_max(LPVOID a);
DWORD WINAPI average(LPVOID a);
int main() {
	printf("Enter size of array ");
	scanf("%d",&N);
	float* arr = (float*)malloc(sizeof(float) * N);

	for (int i = 0; i < N; i++)	{
		printf("arr[%d] = ", i);
		scanf("%f",&arr[i]);
	}

	DWORD IDThread_Min_Max;
	DWORD IDThread_Avarage;

	HANDLE hThread_Min_Max = CreateThread(NULL, 0, min_max,(void*)arr, 0, &IDThread_Min_Max);
	if (hThread_Min_Max == NULL) return GetLastError();
	
	WaitForSingleObject(hThread_Min_Max, INFINITE);
	
	HANDLE hThread_Avarage = CreateThread(NULL, 0, average, (void*)arr, 0, &IDThread_Avarage);
	if (hThread_Avarage == NULL) return GetLastError();

	WaitForSingleObject(hThread_Avarage, INFINITE);

	for (int i = 0; i < N; i++) {
		if (arr[i] == Max || arr[i] == Min) {
			arr[i] = Avarage;
		}
		printf("%.2f ", arr[i]);
	}

	CloseHandle(hThread_Min_Max);
	CloseHandle(hThread_Avarage);
	free(arr);
	getch();
}

DWORD WINAPI average(LPVOID a)
{
	float* arr = (float*)a;
	for (int i = 0; i < N; i++) {
		Avarage += arr[i];
		Sleep(TIME_SLEEP_Avarage);
	}
	Avarage /= N;
	printf("Average is %.2f\n", Avarage);
	return 0;
}

DWORD WINAPI min_max(LPVOID a)
{
	float* arr = (float*)a;

	for (int i = 0; i < N; i++)	{
		if (Max < arr[i])			Max = arr[i];
		Sleep(TIME_SLEEP_MinMax);
		if (Min > arr[i])			Min = arr[i];
		Sleep(TIME_SLEEP_MinMax);
	}
	
	printf("Max - %f, Min -  %.2f\n", Max, Min);
	return 0;
}