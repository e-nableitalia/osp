# osp

Sample application plotting EMG signals and demonstrating offload (host based) signal processing capabilities.

EMGProcessor reads a CSV file with raw EMG signals (encoded in range 0-4096) and plots two graphs:
* raw signal
* filtered signal (applying Open Bionics Sample EMG analisys logic)

# CSV format

Sequence of integers (0-4096) separated by CRLF
