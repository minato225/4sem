#include <windows.h>
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char **argv) {
    FILE *bFile = fopen(argv[1], "a");
    int recordsCount = atoi(argv[2]);
    HANDLE mFileAccess = CreateMutex(NULL, FALSE, "fileAccess");
    HANDLE sStart = CreateSemaphore(NULL, 0, 2, "Start");
    HANDLE sSender = CreateSemaphore(NULL, recordsCount, recordsCount, "Sender");
    HANDLE sReceiver = CreateSemaphore(NULL, 0, 1, "Receiver");

    ReleaseSemaphore(sStart, 1, NULL);
    while (TRUE) {
        printf("Choose option\nWrite - 1, Exit - 0\n");
        int choice = 0;
        scanf("%d", &choice);
        if (choice != 1)return 0;

        char msg[20];
        puts("Enter message");
        scanf("%s", msg);

        WaitForSingleObject(sSender, INFINITE);
        WaitForSingleObject(mFileAccess, INFINITE);

        fprintf(bFile, "%s\n", msg);
        fflush(bFile);

        ReleaseMutex(mFileAccess);
        ReleaseSemaphore(sReceiver, 1, NULL);
    }
}
