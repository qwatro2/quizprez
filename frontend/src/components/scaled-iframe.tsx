import React, {useRef, useEffect, useCallback} from 'react';

interface ScaledIframeProps {
    src: string;
    width?: string | number;
    height?: string | number;
}

const ScaledIframe: React.FC<ScaledIframeProps> = ({src, width = '100%', height = "100%"}) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const iframeRef = useRef<HTMLIFrameElement>(null);

    const updateScale = useCallback(() => {
        const container = containerRef.current;
        const iframeEl = iframeRef.current;
        if (!container || !iframeEl) return;

        try {
            const doc = iframeEl.contentWindow?.document.documentElement;
            if (!doc) return;
            const contentWidth = doc.scrollWidth;
            const containerWidth = container.clientWidth;
            const scale = containerWidth / contentWidth;

            iframeEl.style.transform = `scale(${scale})`;
            iframeEl.style.transformOrigin = 'top left';
            iframeEl.style.width = `${100 / (scale + 0.01)}%`;
            iframeEl.style.height = `${100 / (scale + 0.01)}%`;
        } catch (e) {
            console.warn('Cannot access iframe content:', e);
        }
    }, []);

    useEffect(() => {
        const iframeEl = iframeRef.current;
        if (!iframeEl) return;

        iframeEl.addEventListener('load', updateScale);
        window.addEventListener('resize', updateScale);
        return () => {
            iframeEl.removeEventListener('load', updateScale);
            window.removeEventListener('resize', updateScale);
        };
    }, [updateScale]);

    return (
        <div ref={containerRef}
             style={{width, height, overflowY: 'hidden', overflowX: 'hidden', border: '1px solid #ccc', msOverflowStyle:"none", scrollbarWidth:"none"}}>
            <iframe ref={iframeRef} srcDoc={src} style={{display: 'block', border: 'none'}} title="Scaled Iframe"/>
        </div>
    );
};

export default ScaledIframe;
