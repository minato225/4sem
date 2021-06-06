#define _CRT_SECURE_NO_WARNINGS
#include <Windows.h>
#include <stdio.h>
#include "marker.h"

int main() {
	InitializeCriticalSection(&cs);
	printf("Enter array size: ");
	if (!scanf("%d", &nArrSize))
		return GetLastError();

	nArr = (int*)malloc(sizeof(int) * nArrSize);
	memset(nArr, 0, sizeof(int) * nArrSize);

	printf("Enter count of threads: ");
	if (!scanf("%ud", &threadsCount))
		return GetLastError();

	HANDLE* phArrThreads = (HANDLE*)malloc(sizeof(HANDLE) * threadsCount);
	hArrEventsToStop = (HANDLE*)malloc(sizeof(HANDLE) * threadsCount);

	bArrToTerminate = (BOOL*)malloc(sizeof(BOOL) * threadsCount);
	memset(bArrToTerminate, FALSE, sizeof(BOOL) * threadsCount);

	hStartEvent = CreateEventW(NULL, TRUE, FALSE, NULL);
	hContinueEvent = CreateEventW(NULL, TRUE, FALSE, NULL);


	for (int i = 0; i < threadsCount; i++)	{
		hArrEventsToStop[i] = CreateEventW(NULL, FALSE, FALSE, NULL);
		phArrThreads[i] = CreateThread(NULL, 0, marker, (void*)i, 0, NULL);
	}

	SetEvent(hStartEvent);

	int currThrdsAmnt = threadsCount;
	while (-1 != currThrdsAmnt) {
		WaitForMultipleObjects(threadsCount, hArrEventsToStop, TRUE, INFINITE);

		printf("\nAll threads stop working...\n");

		printArr();
		if (currThrdsAmnt == 0) break;

		int id;
		while (TRUE) {
			printArr();
			printf("\nEnter thread id to stop him: ");

			if (!scanf("%u", &id))
				return GetLastError();

			if (!bArrToTerminate[id - 1])
				break;
		}

		bArrToTerminate[id - 1] = TRUE;
		PulseEvent(hContinueEvent);
		--currThrdsAmnt;
	}

	printf("\nAll threads closed...\n");

	DeleteCriticalSection(&cs);
	CloseThreads(phArrThreads, threadsCount);
	CloseThreads(hArrEventsToStop, threadsCount);

	free(nArr);
	free(phArrThreads);
	free(bArrToTerminate);
	free(hArrEventsToStop);
	return 0;
}