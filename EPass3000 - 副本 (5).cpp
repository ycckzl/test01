#include "StdAfx.h"
#include "EPass3000.h"

EPass3000::EPass3000(void)
{
	CK_RV rv;
	// 加载PKCS库
	rv = C_Initialize(NULL_PTR);
	m_pSlotList = NULL_PTR;
	m_hSession  = NULL;
	m_slotnumber = 0;

	CK_ULONG ulCount = 0;
	rv = C_GetSlotList(TRUE, NULL_PTR, &ulCount);
	if(CKR_OK != rv )
	{
		return;
	}
	if(0 >= ulCount)
	{
		return;
	}

	m_pSlotList = (CK_SLOT_ID_PTR)new CK_SLOT_ID[ulCount];
	if (! m_pSlotList) 
	{
		return;
	}

	rv = C_GetSlotList(TRUE, m_pSlotList, &ulCount);
	if(CKR_OK != rv )
	{
		return;
	}
	if(0 >= ulCount)
	{
		return;
	}

	m_slotnumber = ulCount;
}

EPass3000::~EPass3000(void)
{
	if(m_pSlotList!=NULL)
	{
		delete []m_pSlotList;
		m_pSlotList = NULL;
	}
	C_Finalize(NULL_PTR);
}

BOOL EPass3000::Login(CString username,CString userpin)
{
	if(m_pSlotList == NULL_PTR)
	{
		return FALSE;
	}

	CStringA usea(userpin);

	CK_ULONG ulPIN = usea.GetLength();
	CK_BYTE_PTR pPIN = (CK_BYTE_PTR)usea.GetBuffer(0);

	CK_RV rv;
	rv = C_Login(m_hSession, CKU_USER, pPIN, ulPIN);
	if(CKR_OK != rv)
	{
		return FALSE;
	}
	return TRUE;
}

BOOL EPass3000::LoginByPin(CString userpin)
{
	if(m_pSlotList == NULL_PTR)
	{
		return FALSE;
	}

	BOOL bret = FALSE;
	for(int i=0;i<m_slotnumber;i++)
	{
		if(m_hSession!=NULL)
		{
			C_CloseSession(m_hSession);
			m_hSession = NULL;
		}

		CK_RV rv;
		rv = C_OpenSession(m_pSlotList[i],  CKF_RW_SESSION | CKF_SERIAL_SESSION,
			&m_pApplication, NULL_PTR, &m_hSession);
		if(CKR_OK != rv )
		{
			delete[] m_pSlotList;
			m_pSlotList = NULL_PTR;
			break;
		}

		if(Login(_T(""),userpin))
		{
			bret = TRUE;
			break;
		}
	}	
	return bret;
}

BOOL EPass3000::ReadAll(CString username,CString userpin,int type,BYTE*buff,int&bufflen)
{
	if(m_pSlotList == NULL_PTR)
	{
		return FALSE;
	}

	BOOL bret = FALSE;
	for(int i=0;i<m_slotnumber;i++)
	{
		if(m_hSession!=NULL)
		{
			C_CloseSession(m_hSession);
			m_hSession = NULL;
		}

		CK_RV rv;
		rv = C_OpenSession(m_pSlotList[i],  CKF_RW_SESSION | CKF_SERIAL_SESSION,
			&m_pApplication, NULL_PTR, &m_hSession);
		if(CKR_OK != rv )
		{
			delete[] m_pSlotList;
			m_pSlotList = NULL_PTR;
			break;
		}

		if(Login(username,userpin))
		{
			if(Read(type,buff,bufflen))
			{
				bret = TRUE;
			}
		}
	}	
	return bret;
}

BOOL EPass3000::WriteAll(CString username,CString userpin,int type,BYTE*buff,int bufflen,BOOL bwithcheckkey)
{
	if(m_pSlotList == NULL_PTR)
	{
		return FALSE;
	}

	BOOL bret = FALSE;
	for(int i=0;i<m_slotnumber;i++)
	{
		CK_RV rv;
		rv = C_OpenSession(m_pSlotList[i],  CKF_RW_SESSION | CKF_SERIAL_SESSION,
			&m_pApplication, NULL_PTR, &m_hSession);
		if(CKR_OK != rv )
		{
			delete[] m_pSlotList;
			m_pSlotList = NULL_PTR;
			break;
		}

		if(bwithcheckkey)
		{
			if(Login(username,userpin))
			{
				if(Write(type,buff,bufflen))
				{
					bret = TRUE;
				}
			}
		}
		else
		{
			if(Write(type,buff,bufflen))
			{
				bret = TRUE;
			}
		}		
	}	
	return bret;
}

BOOL EPass3000::Read(int type,BYTE*buff,int&bufflen)
{
	if(m_pSlotList == NULL_PTR)
	{
		return FALSE;
	}

	bufflen = 0;
	CStringA strtype,strApp;
	BOOL bResult = FALSE;
	char* pbData = NULL;
	DWORD cbData = 0;
	CK_ULONG ulRetCount = 0;
	int numObj=0;
	strtype.Format("%d",type);

	CK_OBJECT_CLASS dataClass = CKO_DATA;
	BOOL IsToken=true;
	CK_OBJECT_HANDLE hCKObj;
	CK_CHAR application[] = {"AnXin ePass3000 OBJ 李达"};
	CK_RV ckrv = 0;
	CK_ATTRIBUTE pTemplFind[] = 
	{
		{CKA_CLASS, &dataClass, sizeof(CKO_DATA)},
		{CKA_TOKEN, &IsToken, sizeof(true)}
	};

	C_FindObjectsInit(m_hSession, pTemplFind, 2);
	int i=0;

	while(true)
	{
		ckrv = C_FindObjects(m_hSession, &hCKObj, 1, &ulRetCount);
		if(CKR_OK != ckrv)
		{
			break;	
		}
		if(1 != ulRetCount)
			break;

		CK_ATTRIBUTE pAttrTemp[] = 
		{
			{CKA_CLASS, NULL, 0},
			{CKA_LABEL, NULL, 0},
			{CKA_APPLICATION, NULL, 0},
			{CKA_VALUE, NULL,0},
		};
		ckrv = C_GetAttributeValue(m_hSession, hCKObj, pAttrTemp, 4);
		//	C_DestroyObject(m_hSession, hCKObj);
		if(ckrv != CKR_OK)
		{
			break;
		}
		pAttrTemp[0].pValue = new char[pAttrTemp[0].ulValueLen];
		pAttrTemp[1].pValue = new char[pAttrTemp[1].ulValueLen + 1];
		pAttrTemp[2].pValue = new char[pAttrTemp[2].ulValueLen + 1];
		pAttrTemp[3].pValue = new char[pAttrTemp[3].ulValueLen];
		memset(pAttrTemp[1].pValue,0,pAttrTemp[1].ulValueLen + 1);
		ckrv = C_GetAttributeValue(m_hSession, hCKObj, pAttrTemp, 4);
		if(ckrv != CKR_OK)
		{
			delete[] pAttrTemp[0].pValue;
			delete[] pAttrTemp[1].pValue;
			delete[] pAttrTemp[2].pValue;
			delete[] pAttrTemp[3].pValue;
			break;
		}
		strApp= (char*)pAttrTemp[1].pValue;
		if(strApp == strtype)
		{
			int lenlen = pAttrTemp[3].ulValueLen;
			memcpy(buff,pAttrTemp[3].pValue,lenlen);
			bufflen = lenlen;
			bResult = TRUE;
		}
		delete[] pAttrTemp[0].pValue;
		delete[] pAttrTemp[1].pValue;
		delete[] pAttrTemp[2].pValue;
		delete[] pAttrTemp[3].pValue;
	}
	return bResult;
}

BOOL EPass3000::Write(int type,BYTE*buff,int bufflen)
{
	if(m_pSlotList == NULL_PTR)
	{
		return FALSE;
	}

	BOOL bResult = FALSE;
	CK_ULONG ulRetCount = 0;
	int numObj=0;
	CStringA strtype;
	CStringA strApp;
	strtype.Format("%d",type);

	CK_OBJECT_CLASS dataClass = CKO_DATA;
	BOOL IsToken=true;
	CK_OBJECT_HANDLE hCKObj;
	CK_BBOOL b=0;

	CK_CHAR application[] = {"AnXin ePass3000 OBJ 李达"};
	CK_RV ckrv = 0;
	CK_ATTRIBUTE pTemplFind[] = 
	{
		{CKA_CLASS, &dataClass, sizeof(CKO_DATA)},
		{CKA_TOKEN, &IsToken, sizeof(true)}
	};

	CK_ATTRIBUTE pTemplSave[] = 
	{
		{CKA_CLASS, &dataClass, sizeof(CKO_DATA)},
		{CKA_TOKEN, &IsToken, sizeof(true)},
		{CKA_LABEL,(CK_UTF8CHAR*)(LPSTR)(LPCSTR)strtype,strtype.GetLength() },
		{CKA_APPLICATION, application, sizeof(application)},
		{CKA_PRIVATE,&b,sizeof(b)},
		{CKA_VALUE, (void *)buff, bufflen},
	};

	
	C_FindObjectsInit(m_hSession, pTemplFind, 2);
	int i=0;
	BOOL brun = TRUE;
	while(brun)
	{
		ckrv = C_FindObjects(m_hSession, &hCKObj, 1, &ulRetCount);
		if(CKR_OK != ckrv)
		{
			break;	
		}
		if(1 != ulRetCount)
			break;

		CK_ATTRIBUTE pAttrTemp[] = 
		{
			{CKA_CLASS, NULL, 0},
			{CKA_LABEL, NULL, 0},
			{CKA_APPLICATION, NULL, 0},
			{CKA_VALUE, NULL,0},
		};
		ckrv = C_GetAttributeValue(m_hSession, hCKObj, pAttrTemp, 4);
		if(ckrv != CKR_OK)
		{
			continue;
		}
		pAttrTemp[0].pValue = new char[pAttrTemp[0].ulValueLen];
		pAttrTemp[1].pValue = new char[pAttrTemp[1].ulValueLen + 1];
		pAttrTemp[2].pValue = new char[pAttrTemp[2].ulValueLen + 1];
		pAttrTemp[3].pValue = new char[pAttrTemp[3].ulValueLen];
		memset(pAttrTemp[1].pValue,0,pAttrTemp[1].ulValueLen + 1);
		ckrv = C_GetAttributeValue(m_hSession, hCKObj, pAttrTemp, 4);
		if(ckrv != CKR_OK)
		{
			delete[] pAttrTemp[0].pValue;
			delete[] pAttrTemp[1].pValue;
			delete[] pAttrTemp[2].pValue;
			delete[] pAttrTemp[3].pValue;
			break;
		}
		strApp= (char*)pAttrTemp[1].pValue; 
		if(strApp == strtype)
		{
			ckrv = C_DestroyObject(m_hSession, hCKObj);
			if(ckrv !=CKR_OK)
			{
				bResult = FALSE;
				brun = FALSE;
			}
			else
			{
				ckrv = C_CreateObject(m_hSession,pTemplSave,6,&hCKObj);
				if(ckrv !=CKR_OK)
				{
					bResult = FALSE;
					brun = FALSE;
				}
				else
				{
					bResult = TRUE;
					brun = FALSE;
				}
			}			
		}
		delete[] pAttrTemp[0].pValue;
		delete[] pAttrTemp[1].pValue;
		delete[] pAttrTemp[2].pValue;
		delete[] pAttrTemp[3].pValue;
		i++; 
	}

	if(ulRetCount == 0 && i == 0)
	{
		ckrv = C_CreateObject(m_hSession,pTemplSave,6,&hCKObj);
		if(ckrv ==CKR_OK)
			bResult = TRUE;
	}
	return bResult;
}
