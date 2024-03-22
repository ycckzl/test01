#pragma once

#include <pkcs11/cryptoki.h>
//#include <wincrypt.h>
#pragma comment(lib,"ngp11v211.lib")


class EPass3000
{
public:
	EPass3000(void);
	~EPass3000(void);	

	BOOL ReadAll(CString username,CString userpin,int type,BYTE*buff,int&bufflen);
	BOOL WriteAll(CString username,CString userpin,int type,BYTE*buff,int bufflen,BOOL bwithcheckkey = TRUE);

	BOOL LoginByPin(CString userpin);
private:
	BOOL Read(int type,BYTE*buff,int&bufflen);
	BOOL Write(int type,BYTE*buff,int bufflen);
	BOOL Login(CString username,CString userpin);
private:
	CK_SLOT_ID_PTR    m_pSlotList;
	CK_VOID_PTR       m_pApplication;
	CK_SESSION_HANDLE m_hSession;
	int               m_slotnumber;
};
