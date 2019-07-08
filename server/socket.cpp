#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <cstdlib>
#include <ctime>
#include <vector>

#include <string>

std::vector<std::string> messages;

void connection(int sock)
{
  // Pretty much your C++ code verbatim.
  int msg_index = rand()%messages.size();
  std::string sendMsg = messages[msg_index];
  int amt = send(sock, sendMsg.c_str(), sendMsg.size(), 0);
  printf("Send %d bytes.\n", amt);
  close(sock);
}

int main(int argc, char *argv[])
{
  int sock, csock;
  struct sockaddr_in sin;
  char *host = "192.168.1.131";
  unsigned short port = 1234;

  srand((unsigned)time(0)); 

  messages.push_back("Hello from server \r\n");
  messages.push_back("This is a test message \r\n");
  messages.push_back("A dump message from server \r\n");

  if ((sock = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
    perror("socket");
    exit(EXIT_FAILURE);
  }

  sin.sin_family = AF_INET;
  sin.sin_port = htons(port);
  if (inet_pton(AF_INET, host, &sin.sin_addr) != 1) {
    perror("inet_pton");
    exit(EXIT_FAILURE);
  }

  if (bind(sock, (struct sockaddr*) &sin, sizeof(sin)) != 0) {
    perror("bind");
    exit(EXIT_FAILURE);
  }

  if (listen(sock, SOMAXCONN) != 0) {
    perror("listen");
    exit(EXIT_FAILURE);
  }
  while (true) {
    if ((csock = accept(sock, NULL, NULL)) == -1) {
      perror("accept");
      exit(EXIT_FAILURE);
    }
    connection(csock);
  }
  close(sock);
  return EXIT_SUCCESS;
}