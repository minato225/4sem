#include <windows.h>
#include <stdio.h>

int main() {
    char bFileName[20];
    int recordsCount = 0;
    puts("Enter name of binary file.");
    scanf("%s", bFileName);

    puts("Enter count of records.");
    scanf("%d", &recordsCount);

    FILE *bFile = NULL;
    bFile = fopen(bFileName, "wb");
    if (bFile == NULL) return GetLastError();

    puts("Successful open file");
    puts("Enter count of Senders process.");
    int sendersCount = 0;
    scanf("%d", &sendersCount);

    HANDLE mFileAccess = CreateMutex(NULL, FALSE, "fileAccess");
    HANDLE sStart = CreateSemaphore(NULL, 0, sendersCount, "Start");
    HANDLE sSender = CreateSemaphore(NULL, recordsCount, recordsCount, "Sender");
    HANDLE sReceiver = CreateSemaphore(NULL, 0, 1, "Receiver");

    for (int i = 0; i < sendersCount; ++i) {
        char commandLine[50];
        // sprintf(commandLine, "Sender.exe %s %d", bFileName, recordsCount);
        sprintf(commandLine, "E:\\Lab_4\\Sender\\cmake-build-debug\\Sender.exe %s %d", bFileName, recordsCount);

        STARTUPINFO si;
        ZeroMemory(&si, sizeof(STARTUPINFO));
        si.cb = sizeof(STARTUPINFO);
        PROCESS_INFORMATION pi;
        if (!CreateProcess(NULL, commandLine, NULL, NULL, TRUE, CREATE_NEW_CONSOLE, NULL, NULL, &si, &pi)) {
            puts("Something wrong with create Process.");
            return GetLastError();
        }
    }

    for (int i = 0; i < sendersCount; ++i) WaitForSingleObject(sStart, INFINITE);

    while (TRUE) {
        puts("Choose option\nRead - 1, Exit - 0");
        int choice = 0;
        scanf("%d", &choice);
        if (choice != 1) return 0;

        WaitForSingleObject(sReceiver, INFINITE);
        WaitForSingleObject(mFileAccess, INFINITE);

        bFile = fopen(bFileName, "rb");
        char line[100];
        int msgCount = 0;
        while (fgets(line, 100, bFile)) {
            ++msgCount;
            printf("%s", line);
        }

        if (!fopen(bFileName, "wb")) {
            puts("Fopen error");
            return 0;
        }

        ReleaseMutex(mFileAccess);
        ReleaseSemaphore(sSender, msgCount, NULL);
    }}
