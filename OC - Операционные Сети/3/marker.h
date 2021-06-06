#pragma once
#include <Windows.h>

DWORD CloseThreads(HANDLE* thread_array, int size);
DWORD WINAPI marker(LPVOID iN);
void printArr();

int nArrSize;
int threadsCount;
int* nArr;
HANDLE hStartEvent;
HANDLE hContinueEvent;
HANDLE* hArrEventsToStop;
BOOL* bArrToTerminate;
CRITICAL_SECTION cs;