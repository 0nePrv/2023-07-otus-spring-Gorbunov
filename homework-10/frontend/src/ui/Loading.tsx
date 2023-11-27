import React from 'react';
import '../styles/spinner.css'

export const Loading = () => {
  return (
      <div className={'loadingContainer'}>
        <div className={'spinner'}></div>
      </div>
  );
};
