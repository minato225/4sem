#pragma once
#define _CRT_SECURE_NO_WARNINGS
#include <Windows.h>
#include <stdio.h>

extern int nArrSize = 0;
extern int threadsCount = 0;
extern int* nArr;
extern HANDLE hStartEvent;
extern HANDLE hContinueEvent;
extern HANDLE* hArrEventsToStop;
extern BOOL* bArrToTerminate;
extern CRITICAL_SECTION cs;

DWORD WINAPI marker(LPVOID number) {
	int nID = (int)number;
	int markedItems = 0;
	int* nArrToInit = (int*)malloc(sizeof(int) * nArrSize);
	memset(nArrToInit, -1, sizeof(int) * nArrSize);
	srand(nID + 1);
	printf("Thread %u created...\n", nID + 1);

	WaitForSingleObject(hStartEvent, INFINITE);

	int curI = 0;
	while (TRUE)	{
		int nRundomID = rand() % nArrSize;
		EnterCriticalSection(&cs);
		if (nArr[nRundomID] == 0) {

			Sleep(5);
			nArr[nRundomID] = nID + 1;
			Sleep(5);

			LeaveCriticalSection(&cs);
			nArrToInit[curI++] = nRundomID;
			markedItems++;
		}
		else
		{
			if (markedItems > 0) {
				printf("\t\nThread id %u\n", nID + 1);
				printf("\tElements marked %u\n", markedItems);
				printf("\tnext unmarkable index %u\n", nRundomID + 1);
				printf("\tChanged Indexes: ");
				for (int j = 0; j < nArrSize; j++)
					if (nArrToInit[j] != -1)
						printf("%d ", nArrToInit[j] + 1);
				printf("\n");
			}

			LeaveCriticalSection(&cs);

			SignalObjectAndWait(hArrEventsToStop[nID], hContinueEvent, INFINITE, FALSE);

			if (bArrToTerminate[nID]) {

				EnterCriticalSection(&cs);
				int i = 0;
				while (nArrToInit[i] != -1)
					nArr[nArrToInit[i++]] = 0;
				
				LeaveCriticalSection(&cs);

				printf("\t\t\t\t\tThread %u is dead...\n", nID + 1);
				
				SetEvent(hArrEventsToStop[nID]);
				break;
			}
		}
	}

	free(nArrToInit);
	return 0;
}

void printArr() {
	printf("Array\n");
	for (int i = 0; i < nArrSize; i++)
		printf("%u ", nArr[i]);
	printf("\n");
}

DWORD CloseThreads(HANDLE* thread_array, int size) {
	for (int i = 0; i < size; i++) {
		DWORD result = CloseHandle(thread_array[i]);
		if (NULL == result)
			return GetLastError();
	}
	return EXIT_SUCCESS;
}
