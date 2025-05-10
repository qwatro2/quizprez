import {useEffect, useRef, useState} from 'react';
import Editor from '@monaco-editor/react';
import {Box, Divider, IconButton, Paper, Tooltip, useTheme} from '@mui/material';
import {ContentCopy, Download, FormatIndentIncrease, Refresh} from '@mui/icons-material';

export const SplitHtmlEditor = () => {
    const theme = useTheme();
    const [htmlCode, setHtmlCode] = useState<string>(
        `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MUI HTML Editor</title>
  <style>
    body {
      font-family: 'Roboto', sans-serif;
      margin: 0;
      padding: 24px;
      background-color: ${theme.palette.mode === 'dark' ? '#121212' : '#f5f5f5'};
      color: ${theme.palette.mode === 'dark' ? '#fff' : '#333'};
    }
    h1 {
      color: ${theme.palette.primary.main};
    }
  </style>
</head>
<body>
  <h1>Material UI Split Editor</h1>
  <p>Edit HTML on the left and see changes in real-time on the right</p>
</body>
</html>`
    );

    const iframeRef = useRef<HTMLIFrameElement>(null);

    useEffect(() => {
        if (iframeRef.current) {
            iframeRef.current.srcdoc = htmlCode;
        }
    }, [htmlCode]);

    const handleEditorChange = (value: string | undefined) => {
        value && setHtmlCode(value);
    };

    const handleFormatCode = () => {
        alert('Use Ctrl+Shift+F in editor to format code');
    };

    const handleDownload = () => {
        const blob = new Blob([htmlCode], {type: 'text/html'});
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'index.html';
        a.click();
        URL.revokeObjectURL(url);
    };

    const handleCopyCode = () => {
        navigator.clipboard.writeText(htmlCode);
    };

    const handleRefreshPreview = () => {
        if (iframeRef.current) {
            iframeRef.current.srcdoc = htmlCode;
        }
    };

    return (
        <Box sx={{
            display: 'flex',
            flexDirection: 'column',
            height: '100vh',
            bgcolor: 'background.default'
        }}>
            <Paper elevation={3} sx={{
                p: 1,
                display: 'flex',
                justifyContent: 'flex-end',
                gap: 1
            }}>
                <Tooltip title="Format Code">
                    <IconButton onClick={handleFormatCode}>
                        <FormatIndentIncrease/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Copy Code">
                    <IconButton onClick={handleCopyCode}>
                        <ContentCopy/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Download HTML">
                    <IconButton onClick={handleDownload}>
                        <Download/>
                    </IconButton>
                </Tooltip>
                <Tooltip title="Refresh Preview">
                    <IconButton onClick={handleRefreshPreview}>
                        <Refresh/>
                    </IconButton>
                </Tooltip>
            </Paper>

            <Divider/>

            <Box sx={{display: 'flex', flex: 1, overflow: 'hidden'}}>
                <Box sx={{
                    width: '50%',
                    height: '100%',
                    borderRight: `1px solid ${theme.palette.divider}`
                }}>
                    <Editor
                        height="100%"
                        language="html"
                        value={htmlCode}
                        onChange={handleEditorChange}
                        options={{
                            minimap: {enabled: false},
                            fontSize: 14,
                            wordWrap: 'on',
                            autoClosingQuotes: 'always',
                            formatOnPaste: true,
                            formatOnType: true,
                            tabSize: 2,
                            scrollBeyondLastLine: false,
                            renderWhitespace: 'none',
                            padding: {top: 20, bottom: 20},
                            automaticLayout: true,
                        }}
                        theme={theme.palette.mode === 'dark' ? 'vs-dark' : 'light'}
                    />
                </Box>

                <Box sx={{width: '50%', height: '100%'}}>
                    <Box
                        component="iframe"
                        ref={iframeRef}
                        title="HTML Preview"
                        sx={{
                            width: '100%',
                            height: '100%',
                            border: 'none',
                            bgcolor: 'background.paper'
                        }}
                        sandbox="allow-scripts allow-same-origin"
                    />
                </Box>
            </Box>
        </Box>
    );
};

export default SplitHtmlEditor